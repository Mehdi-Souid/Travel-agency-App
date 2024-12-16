package org.example.demo.Garbage;

import org.example.demo.AdminApplication;
import org.example.demo.HelloApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
    JMenuItem menuitemUser ;
    JMenuItem menuitemAdmin;

    Menu() {
        // Configuration de la fenêtre principale
        setTitle("SkyWing Airlines");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);




        JMenuBar Menubar = new JMenuBar();
        JMenu menuApp = new JMenu("Connect");
        JMenu menuAbout = new JMenu("About");
        menuitemUser = new JMenuItem("User");
        menuitemAdmin = new JMenuItem("Admin");
        menuApp.add(menuitemUser);
        menuApp.add(menuitemAdmin);
        Menubar.add(menuApp);
        Menubar.add(menuAbout);
        this.setJMenuBar(Menubar);

        menuitemUser.addActionListener(this);
        menuitemAdmin.addActionListener(this);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==menuitemUser){
            HelloApplication.launch(HelloApplication.class);
        }
        if(e.getSource()==menuitemAdmin){
            AdminApplication.launch(AdminApplication.class);

        }

    }
}
