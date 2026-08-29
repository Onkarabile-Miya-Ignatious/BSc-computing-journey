import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class HospitalWaitingSystemGUI extends JFrame {

    // THEME COLORS  
    static final Color NAVY        = new Color(13, 27, 62);
    static final Color NAVY_LIGHT  = new Color(24, 44, 94);
    static final Color ELECTRIC    = new Color(0, 194, 168);
    static final Color VIOLET      = new Color(124, 77, 255);
    static final Color CRITICAL_RED     = new Color(230, 57, 70);
    static final Color EMERGENCY_ORANGE = new Color(255, 133, 27);
    static final Color MODERATE_YELLOW  = new Color(255, 202, 40);
    static final Color LOW_GREEN        = new Color(46, 196, 108);
    static final Color BG          = new Color(244, 246, 250);
    static final Color WHITE       = Color.WHITE;

    static final Font TITLE_FONT  = new Font("Segoe UI", Font.BOLD, 24);
    static final Font NAV_FONT    = new Font("Segoe UI", Font.BOLD, 15);
    static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    static final Font BODY_FONT   = new Font("Segoe UI", Font.PLAIN, 14);

   // DATA
    // static so patient records persist across logout/login (shared hospital database,
    // not per-session data) — this is what was resetting when you switched roles
    private static final List<Patient> patients = new ArrayList<>();
    private String currentRole = "Receptionist";

    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> patientPicker;

    private CardLayout cards;
    private JPanel cardPanel;
    private JPanel sidebar;
    private final List<NavButton> navButtons = new ArrayList<>();

    public HospitalWaitingSystemGUI(String role) {
        this.currentRole = role;
        setTitle("Intelligent Hospital Waiting System \u2014 NextGen Medical (" + role + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildGradientHeader(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildCardArea(), BorderLayout.CENTER);

        // Populate the table/picker immediately with existing records on login,
        // instead of waiting for the first Add/Assess/Refresh action
        refreshTable();
        refreshPatientPicker();
    }


    // HEADER

    private JPanel buildGradientHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, NAVY, getWidth(), 0, VIOLET);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(100, 80));
        header.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel title = new JLabel("INTELLIGENT HOSPITAL WAITING SYSTEM");
        title.setFont(TITLE_FONT);
        title.setForeground(WHITE);

        JLabel role = new JLabel(currentRole.toUpperCase() + " \u25CF LOGGED IN");
        role.setFont(BODY_FONT);
        role.setForeground(ELECTRIC);
        role.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(title, BorderLayout.WEST);
        header.add(role, BorderLayout.EAST);
        return header;
    }


    // SIDEBAR NAVIGATION

    private JPanel buildSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(220, 100));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        addNavButton("Add Patient", ELECTRIC, "ADD", currentRole.equals("Receptionist"));
        addNavButton("Assess & Assign", VIOLET, "ASSESS", currentRole.equals("Receptionist"));
        addNavButton("Waiting List", MODERATE_YELLOW, "LIST", true);
        addNavButton("Predict Wait Time", LOW_GREEN, "WAIT", true);

        sidebar.add(Box.createVerticalGlue());

        NavButton logout = new NavButton("Logout", CRITICAL_RED, "LOGOUT");
        logout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(HospitalWaitingSystemGUI::launchLogin);
        });
        sidebar.add(logout);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private void addNavButton(String label, Color accent, String cardKey, boolean enabled) {
        NavButton btn = new NavButton(label, accent, cardKey);
        if (!enabled) {
            btn.setEnabled(false);
            btn.setToolTipText("Managers do not have access to this section");
        } else {
            btn.addActionListener(e -> {
                cards.show(cardPanel, cardKey);
                for (NavButton nb : navButtons) nb.setSelected(nb == btn);
            });
        }
        navButtons.add(btn);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(4));
    }


    // CARD AREA (main content, swapped by sidebar clicks)

    private JPanel buildCardArea() {
        cards = new CardLayout();
        cardPanel = new JPanel(cards);
        cardPanel.setBackground(BG);

        cardPanel.add(buildAddPatientTab(), "ADD");
        cardPanel.add(buildAssessTab(), "ASSESS");
        cardPanel.add(buildWaitingListTab(), "LIST");
        cardPanel.add(buildWaitTimeTab(), "WAIT");

        cards.show(cardPanel, currentRole.equals("Receptionist") ? "ADD" : "LIST");
        if (!navButtons.isEmpty()) {
            NavButton toSelect = currentRole.equals("Receptionist") ? navButtons.get(0) : navButtons.get(2);
            toSelect.setSelected(true);
        }
        return cardPanel;
    }


    // TAB: ADD PATIENT

    private JPanel buildAddPatientTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addSectionTitle(panel, gbc, 0, "Add a New Patient", ELECTRIC);

        JTextField nameField = styledField();
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 999999, 1));

        addFormRow(panel, gbc, 1, "Full Name:", nameField);
        addFormRow(panel, gbc, 2, "Age:", ageSpinner);
        addFormRow(panel, gbc, 3, "Patient ID:", idSpinner);

        SolidButton addBtn = new SolidButton("Add Patient", ELECTRIC);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(addBtn, gbc);

        JLabel status = new JLabel(" ");
        status.setFont(BODY_FONT);
        gbc.gridy = 5;
        panel.add(status, gbc);

        addBtn.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            int age = (Integer) ageSpinner.getValue();
            int id = (Integer) idSpinner.getValue();

            if (name.length() < 3) {
                status.setForeground(CRITICAL_RED);
                status.setText("Name must be at least 3 characters.");
                return;
            }
            if (patients.size() >= 15) {
                status.setForeground(CRITICAL_RED);
                status.setText("Hospital is full right now (max 15 patients).");
                return;
            }
            boolean duplicateId = patients.stream().anyMatch(p -> p.id == id);
            if (duplicateId) {
                status.setForeground(CRITICAL_RED);
                status.setText("Patient ID " + id + " is already in use \u2014 please choose a unique ID.");
                return;
            }

            Patient p = new Patient(name.toUpperCase(), age, id);
            patients.add(p);
            refreshPatientPicker();
            refreshTable();

            status.setForeground(LOW_GREEN);
            status.setText("Patient " + p.name + " added successfully.");
            nameField.setText("");
            idSpinner.setValue(id + 1);
        });

        return panel;
    }


    // TAB: ASSESS SYMPTOMS & ASSIGN DOCTOR

    private JPanel buildAssessTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addSectionTitle(panel, gbc, 0, "Assess Symptoms & Assign Doctor", VIOLET);

        patientPicker = new JComboBox<>();
        patientPicker.setFont(BODY_FONT);
        addFormRow(panel, gbc, 1, "Select Patient:", patientPicker);

        JLabel keywordsLbl = new JLabel("Keywords: CHEST, BREATHING, BLEEDING, STROKE, UNCONSCIOUS, FEVER, HEADACHE");
        keywordsLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        keywordsLbl.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(keywordsLbl, gbc);
        gbc.gridwidth = 1;

        JTextField symptomsField = styledField();
        addFormRow(panel, gbc, 3, "Symptoms:", symptomsField);

        SolidButton assignBtn = new SolidButton("Assess & Assign", VIOLET);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(assignBtn, gbc);

        JTextArea reportArea = new JTextArea(8, 30);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        reportArea.setBackground(WHITE);
        reportArea.setBorder(BorderFactory.createLineBorder(VIOLET, 2));
        JScrollPane scroll = new JScrollPane(reportArea);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scroll, gbc);

        assignBtn.addActionListener((ActionEvent e) -> {
            if (patientPicker.getSelectedIndex() < 0 || patients.isEmpty()) {
                reportArea.setText("No patient selected. Add a patient first.");
                return;
            }
            Patient p = patients.get(patientPicker.getSelectedIndex());
            String symptoms = symptomsField.getText().trim().toUpperCase();

            int score = calculateUrgency(symptoms);
            String[] result = assignDoctor(symptoms);

            p.score = score;
            p.status = result[0];
            p.doctor = result[1];

            bubbleSortByScore(patients);
            refreshTable();
            refreshPatientPicker();

            reportArea.setText(
                    "----------- REPORT -----------\n"
                            + "Patient:         " + p.name + "\n"
                            + "Symptoms Logged: " + symptoms + "\n"
                            + "Priority Score:  " + score + "\n"
                            + "Status:          " + p.status + "\n"
                            + "Assigned Doctor: " + p.doctor + "\n"
                            + "-------------------------------\n"
                            + "Queue re-sorted. Critical patients moved to the front."
            );
        });

        return panel;
    }


    // TAB: WAITING LIST

    private JPanel buildWaitingListTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLbl = new JLabel("Current Waiting List");
        titleLbl.setFont(HEADER_FONT);
        titleLbl.setForeground(NAVY);
        titleLbl.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(titleLbl, BorderLayout.NORTH);

        String[] columns = {"No.", "Name", "Age", "ID", "Doctor", "Score", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(BODY_FONT);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(NAV_FONT);
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(WHITE);
        table.setDefaultRenderer(Object.class, new StatusRowRenderer());
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230)));
        panel.add(scroll, BorderLayout.CENTER);

        SolidButton refreshBtn = new SolidButton("Refresh / Re-sort", MODERATE_YELLOW);
        refreshBtn.setDarkText(true);
        refreshBtn.addActionListener(e -> {
            bubbleSortByScore(patients);
            refreshTable();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(BG);
        bottom.add(refreshBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // TAB: PREDICT WAIT TIME
    private JPanel buildWaitTimeTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addSectionTitle(panel, gbc, 0, "Predict Wait Time", LOW_GREEN);

        JSpinner positionSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 15, 1));
        addFormRow(panel, gbc, 1, "Position in Queue:", positionSpinner);

        SolidButton predictBtn = new SolidButton("Predict Wait Time", LOW_GREEN);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(predictBtn, gbc);

        JLabel resultLbl = new JLabel(" ");
        resultLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        resultLbl.setForeground(NAVY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(resultLbl, gbc);

        predictBtn.addActionListener(e -> {
            int pos = (Integer) positionSpinner.getValue();
            int minutes = calculateWaitTime(pos);
            resultLbl.setText("\u23F1  Estimated wait: " + minutes + " minutes");
        });

        return panel;
    }



    private int calculateWaitTime(int place) {
        if (place == 1) return 15;
        return 15 + calculateWaitTime(place - 1);
    }

    private int calculateUrgency(String symptoms) {
        int score = 5;
        if (symptoms.contains("CHEST")) score += 50;
        if (symptoms.contains("BREATHING")) score += 40;
        if (symptoms.contains("BLEEDING")) score += 30;
        if (symptoms.contains("STROKE")) score += 50;
        if (symptoms.contains("UNCONSCIOUS")) score += 50;
        if (symptoms.contains("FEVER")) score += 15;
        if (symptoms.contains("HEADACHE")) score += 10;
        return score;
    }

    private String[] assignDoctor(String symptoms) {
        if (symptoms.contains("CHEST")) {
            return new String[]{"CRITICAL EMERGENCY", "Dr. Nongqunga (Cardiologist)"};
        } else if (symptoms.contains("BREATHING")) {
            return new String[]{"EMERGENCY", "Dr. Mkhwanazi (Pulmonologist)"};
        } else if (symptoms.contains("BLEEDING")) {
            return new String[]{"EMERGENCY", "Dr. Kesiyane (Trauma Surgeon)"};
        } else if (symptoms.contains("STROKE") || symptoms.contains("UNCONSCIOUS")) {
            return new String[]{"CRITICAL EMERGENCY", "Dr. Magabe (Neurologist)"};
        } else if (symptoms.contains("FEVER")) {
            return new String[]{"MODERATE", "Dr. Motaung (General Practitioner)"};
        } else {
            return new String[]{"LOW / NORMAL", "Dr. Miya (General Practitioner)"};
        }
    }

    private void bubbleSortByScore(List<Patient> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).score < list.get(j + 1).score) {
                    Patient temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }


    // UI HELPERS

    private void refreshTable() {
        tableModel.setRowCount(0);
        int serial = 1;
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{serial++, p.name, p.age, p.id, p.doctor, p.score, p.status});
        }
    }

    private void refreshPatientPicker() {
        patientPicker.removeAllItems();
        for (Patient p : patients) {
            patientPicker.addItem(p.name + " (ID: " + p.id + ")");
        }
    }

    private JTextField styledField() {
        JTextField field = new JTextField(18);
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 214)),
                new EmptyBorder(6, 8, 6, 8)));
        return field;
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, int row, String text, Color accent) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(accent);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        panel.add(lbl, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(NAV_FONT);
        lbl.setForeground(NAVY);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /** Color-codes each waiting-list row solidly based on the Status column. */
    private class StatusRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String status = String.valueOf(table.getModel().getValueAt(row, 6));

            Color bg;
            Color fg;
            switch (status) {
                case "CRITICAL EMERGENCY": bg = CRITICAL_RED; fg = WHITE; break;
                case "EMERGENCY": bg = EMERGENCY_ORANGE; fg = WHITE; break;
                case "MODERATE": bg = MODERATE_YELLOW; fg = Color.BLACK; break;
                case "LOW / NORMAL": bg = LOW_GREEN; fg = WHITE; break;
                default: bg = row % 2 == 0 ? WHITE : new Color(248, 249, 251); fg = Color.BLACK;
            }
            setOpaque(true);
            if (!isSelected) {
                c.setBackground(bg);
                c.setForeground(fg);
            }
            setBorder(new EmptyBorder(0, 10, 0, 10));
            return c;
        }
    }


    // MAIN + LOGIN LAUNCH

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalWaitingSystemGUI::launchLogin);
    }

    private static void launchLogin() {
        LoginDialog login = new LoginDialog();
        login.setVisible(true);
        if (login.isAuthenticated()) {
            new HospitalWaitingSystemGUI(login.getRole()).setVisible(true);
        }
    }
}

// LOGIN DIALOG

class LoginDialog extends JDialog {

    private static final String RECEPTIONIST_PASSWORD = "NEXTGEN15";
    private static final String MANAGER_PASSWORD = "MANAGER123";
    private static final int MAX_ATTEMPTS = 3;

    private boolean authenticated = false;
    private String role = "Receptionist";
    private int attempts = 0;

    private String pendingRole = null;

    LoginDialog() {
        setTitle("Secure Login \u2014 NextGen Medical");
        setModal(true);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, HospitalWaitingSystemGUI.NAVY,
                        0, getHeight(), HospitalWaitingSystemGUI.VIOLET);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(content);

        JLabel title = new JLabel("NEXTGEN MEDICAL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Hospital Waiting System \u2014 Secure Access");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 215, 240));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(4));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(30));

        CardLayout roleCards = new CardLayout();
        JPanel switchPanel = new JPanel(roleCards);
        switchPanel.setOpaque(false);

        JPanel rolePanel = new JPanel();
        rolePanel.setOpaque(false);
        rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.Y_AXIS));

        SolidButton receptionistBtn = new SolidButton("Login as Receptionist", HospitalWaitingSystemGUI.ELECTRIC);
        receptionistBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        receptionistBtn.setMaximumSize(new Dimension(300, 45));

        SolidButton managerBtn = new SolidButton("Login as Manager", HospitalWaitingSystemGUI.MODERATE_YELLOW);
        managerBtn.setDarkText(true);
        managerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        managerBtn.setMaximumSize(new Dimension(300, 45));

        rolePanel.add(receptionistBtn);
        rolePanel.add(Box.createVerticalStrut(15));
        rolePanel.add(managerBtn);

        JPanel pwPanel = new JPanel();
        pwPanel.setOpaque(false);
        pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.Y_AXIS));

        JLabel pwLabel = new JLabel("Enter Password:");
        pwLabel.setForeground(Color.WHITE);
        pwLabel.setFont(HospitalWaitingSystemGUI.NAV_FONT);
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField pwField = new JPasswordField(16);
        pwField.setMaximumSize(new Dimension(260, 34));
        pwField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel attemptsLbl = new JLabel(" ");
        attemptsLbl.setForeground(HospitalWaitingSystemGUI.CRITICAL_RED);
        attemptsLbl.setFont(HospitalWaitingSystemGUI.BODY_FONT);
        attemptsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        SolidButton submitBtn = new SolidButton("Access Terminal", HospitalWaitingSystemGUI.LOW_GREEN);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(220, 42));

        SolidButton backBtn = new SolidButton("\u2190 Back", HospitalWaitingSystemGUI.NAVY_LIGHT);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(120, 34));

        pwPanel.add(pwLabel);
        pwPanel.add(Box.createVerticalStrut(8));
        pwPanel.add(pwField);
        pwPanel.add(Box.createVerticalStrut(10));
        pwPanel.add(submitBtn);
        pwPanel.add(Box.createVerticalStrut(10));
        pwPanel.add(attemptsLbl);
        pwPanel.add(Box.createVerticalStrut(10));
        pwPanel.add(backBtn);

        switchPanel.add(rolePanel, "ROLE");
        switchPanel.add(pwPanel, "PASSWORD");
        content.add(switchPanel);

        receptionistBtn.addActionListener(e -> {
            pendingRole = "Receptionist";
            pwField.setText("");
            attemptsLbl.setText(" ");
            roleCards.show(switchPanel, "PASSWORD");
        });
        managerBtn.addActionListener(e -> {
            pendingRole = "Manager";
            pwField.setText("");
            attemptsLbl.setText(" ");
            roleCards.show(switchPanel, "PASSWORD");
        });
        backBtn.addActionListener(e -> roleCards.show(switchPanel, "ROLE"));

        ActionListener tryLogin = e -> {
            String entered = new String(pwField.getPassword());
            String correct = pendingRole.equals("Receptionist") ? RECEPTIONIST_PASSWORD : MANAGER_PASSWORD;

            if (entered.equals(correct)) {
                authenticated = true;
                role = pendingRole;
                dispose();
            } else {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    attemptsLbl.setText("Too many failed attempts. Access denied.");
                    Timer t = new Timer(1400, ev -> {
                        authenticated = false;
                        dispose();
                    });
                    t.setRepeats(false);
                    t.start();
                } else {
                    attemptsLbl.setText("Wrong password. Attempts left: " + (MAX_ATTEMPTS - attempts));
                }
                pwField.setText("");
            }
        };
        submitBtn.addActionListener(tryLogin);
        pwField.addActionListener(tryLogin);
    }

    boolean isAuthenticated() {
        return authenticated;
    }

    String getRole() {
        return role;
    }
}

class SolidButton extends JButton {

    private final Color baseColor;
    private boolean darkText = false;
    private boolean hovering = false;
    private boolean pressed = false;

    SolidButton(String text, Color color) {
        super(text);
        this.baseColor = color;
        setFont(HospitalWaitingSystemGUI.NAV_FONT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(12, 22, 12, 22));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
            @Override
            public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
            @Override
            public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    void setDarkText(boolean darkText) {
        this.darkText = darkText;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = baseColor;
        if (pressed) fill = fill.darker();
        else if (hovering) fill = brighten(fill);

        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.dispose();
        super.paintComponent(g);
    }

    private Color brighten(Color c) {
        int r = Math.min(255, c.getRed() + 25);
        int gg = Math.min(255, c.getGreen() + 25);
        int b = Math.min(255, c.getBlue() + 25);
        return new Color(r, gg, b);
    }

    @Override
    public Color getForeground() {
        return darkText ? Color.BLACK : Color.WHITE;
    }
}


// SIDEBAR NAV BUTTON (highlighted accent bar when selected)
class NavButton extends JButton {

    private final Color accent;
    private boolean selected = false;
    private boolean hovering = false;

    NavButton(String text, Color accent, String cardKey) {
        super(text);
        this.accent = accent;
        setFont(HospitalWaitingSystemGUI.NAV_FONT);
        setHorizontalAlignment(SwingConstants.LEFT);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(14, 24, 14, 14));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(220, 50));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
        });
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (selected) {
            g2.setColor(HospitalWaitingSystemGUI.NAVY_LIGHT);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(accent);
            g2.fillRect(0, 0, 5, getHeight());
        } else if (hovering && isEnabled()) {
            g2.setColor(HospitalWaitingSystemGUI.NAVY_LIGHT);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Color getForeground() {
        return isEnabled() ? Color.WHITE : new Color(120, 128, 150);
    }
}

// PATIENT MODEL

class Patient {
    String name;
    int age;
    int id;
    String doctor = "Not Assigned Yet";
    int score = 0;
    String status = "NOT ASSESSED";

    Patient(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }
