/*
 * QCSQLQueryBuilderPanel.java
 *
 * Created on July 19, 2000, 6:46 PM
 *
 * This is free open source software distributed under the IBM Public License found
 * on the World Wide Web at http://oss.software.ibm.com/developerworks/opensource/license10.html
 * Copyright *C* 2000, Jack J. Woehr, PO Box 51, Golden, CO 80402-0051 USA jax@well.com
 * Copyright *C* 2000, International Business Machines Corporation and others. All Rights Reserved.
 */

package com.SoftWoehr.JTOpenContrib.QCDemo;

/** A panel to display an SQLQueryBuilder.
 * @author jax
 * @version 1.0
 */
public class QCSQLQueryBuilderPanel extends javax.swing.JPanel {

  private com.ibm.as400.vaccess.SQLConnection sqlConnection;
  private com.ibm.as400.vaccess.ErrorDialogAdapter errorDialogAdapter;
  private boolean useCustomDriver = false;
  private QCSQLResultSetTablePanel resultTable;
  private QCSQLResultSetFormPanel resultForm;
  private java.util.Vector classesInstanced = new java.util.Vector(4,4);

  private javax.swing.ButtonGroup buttonGroup;

  /** Creates new form QCSQLQueryBuilderPanel */
  public QCSQLQueryBuilderPanel() {
    initComponents ();
    establishButtonGroup();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents () {//GEN-BEGIN:initComponents
    sessionPanel = new javax.swing.JPanel ();
    urlPanel = new javax.swing.JPanel ();
    urlTextField = new javax.swing.JTextField ();
    connectButton = new javax.swing.JButton ();
    driverTypePanel = new javax.swing.JPanel ();
    as400RadioButton = new javax.swing.JRadioButton ();
    sunJDBCRadioButton4 = new javax.swing.JRadioButton ();
    postgresqlRadioButton = new javax.swing.JRadioButton ();
    customRadioButton = new javax.swing.JRadioButton ();
    driverClassNameTextField = new javax.swing.JTextField ();
    sQLQueryBuilderPane1 = new com.ibm.as400.vaccess.SQLQueryBuilderPane ();
    queryButtonPanel = new javax.swing.JPanel ();
    queryToTableButton = new javax.swing.JButton ();
    queryToFormButton = new javax.swing.JButton ();
    setLayout (new java.awt.BorderLayout ());

    sessionPanel.setLayout (new java.awt.GridLayout (2, 1));

      urlPanel.setLayout (new java.awt.GridLayout (2, 1));

        urlTextField.setToolTipText ("Enter a valid SQLConnection URL of the form jdbc:<driver>://<system_name>");
        urlTextField.setText ("jdbc:");

        urlPanel.add (urlTextField);

        connectButton.setToolTipText ("Connects to the JDBC URL you entered above.");
        connectButton.setText ("Connect Now");
        connectButton.addActionListener (new java.awt.event.ActionListener () {
          public void actionPerformed (java.awt.event.ActionEvent evt) {
            connectButtonActionPerformed (evt);
          }
        }
        );

        urlPanel.add (connectButton);

      sessionPanel.add (urlPanel);

      driverTypePanel.setLayout (new java.awt.GridLayout (1, 5));
      driverTypePanel.setBorder (new javax.swing.border.TitledBorder("Set driver type in URL"));

        as400RadioButton.setToolTipText ("Edits the URL above to reflect your driver choice. Feel free to customize the URL afterwards.");
        as400RadioButton.setBorder (new javax.swing.border.TitledBorder(""));
        as400RadioButton.setText ("JTOpen");
        as400RadioButton.addActionListener (new java.awt.event.ActionListener () {
          public void actionPerformed (java.awt.event.ActionEvent evt) {
            as400RadioButtonActionPerformed (evt);
          }
        }
        );

        driverTypePanel.add (as400RadioButton);

        sunJDBCRadioButton4.setToolTipText ("Edits the URL above to reflect your driver choice. Feel free to customize the URL afterwards.");
        sunJDBCRadioButton4.setText ("Sun JDBC");
        sunJDBCRadioButton4.addActionListener (new java.awt.event.ActionListener () {
          public void actionPerformed (java.awt.event.ActionEvent evt) {
            sunJDBCRadioButton4ActionPerformed (evt);
          }
        }
        );

        driverTypePanel.add (sunJDBCRadioButton4);

        postgresqlRadioButton.setToolTipText ("Edits the URL above to reflect your driver choice. Feel free to customize the URL afterwards.");
        postgresqlRadioButton.setText ("PostgreSQL");
        postgresqlRadioButton.addActionListener (new java.awt.event.ActionListener () {
          public void actionPerformed (java.awt.event.ActionEvent evt) {
            postgresqlRadioButtonActionPerformed (evt);
          }
        }
        );

        driverTypePanel.add (postgresqlRadioButton);

        customRadioButton.setToolTipText ("Please enter driver name in the entry field next to the button.");
        customRadioButton.setText ("Custom");
        customRadioButton.addActionListener (new java.awt.event.ActionListener () {
          public void actionPerformed (java.awt.event.ActionEvent evt) {
            customRadioButtonActionPerformed (evt);
          }
        }
        );

        driverTypePanel.add (customRadioButton);


        driverTypePanel.add (driverClassNameTextField);

      sessionPanel.add (driverTypePanel);


    add (sessionPanel, java.awt.BorderLayout.NORTH);



    add (sQLQueryBuilderPane1, java.awt.BorderLayout.CENTER);

    queryButtonPanel.setLayout (new java.awt.GridLayout (1, 2));

      queryToTableButton.setToolTipText ("Shows the results of the query in table form on the SQL Result Table tab.");
      queryToTableButton.setText ("Do Query with Results to Table");
      queryToTableButton.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          queryToTableButtonActionPerformed (evt);
        }
      }
      );

      queryButtonPanel.add (queryToTableButton);

      queryToFormButton.setToolTipText ("Shows the results of the query in table form on the SQL Result Form  tab.");
      queryToFormButton.setText ("Do Query with Results to Form");
      queryToFormButton.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          queryToFormButtonActionPerformed (evt);
        }
      }
      );

      queryButtonPanel.add (queryToFormButton);


    add (queryButtonPanel, java.awt.BorderLayout.SOUTH);

  }//GEN-END:initComponents

  private void queryToFormButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryToFormButtonActionPerformed
    // Add your handling code here:
    if (null != sQLQueryBuilderPane1.getConnection() & null != sQLQueryBuilderPane1.getQuery()) {
      resultForm.doQuery(sQLQueryBuilderPane1.getConnection(), sQLQueryBuilderPane1.getQuery());
    }
  }//GEN-LAST:event_queryToFormButtonActionPerformed

  private void queryToTableButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryToTableButtonActionPerformed
    // Add your handling code here:
    if (null != sQLQueryBuilderPane1.getConnection() & null != sQLQueryBuilderPane1.getQuery()) {
      resultTable.doQuery(sQLQueryBuilderPane1.getConnection(), sQLQueryBuilderPane1.getQuery());
    }
  }//GEN-LAST:event_queryToTableButtonActionPerformed

  private void connectButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
    // Add your handling code here:

    // Free resources by closing previous connection.
    if (null != sQLQueryBuilderPane1.getConnection()) {
      try {
        sQLQueryBuilderPane1.getConnection().close();
      }
      catch (java.sql.SQLException e) {
        e.printStackTrace(System.err);
      }
    }

    // Process the URL.
    QCJdbcURL url = new QCJdbcURL(urlTextField.getText());
    urlTextField.setText(url.getURL());

    // Load the driver if necessary.
    try {
      instanceDriver(url.driver);
    }
    catch (java.lang.ClassNotFoundException e) {
      e.printStackTrace(System.err);
    }
    catch (java.sql.SQLException e) {
      e.printStackTrace(System.err);
    }

    // Attempt the connection.
    sqlConnection = new com.ibm.as400.vaccess.SQLConnection(urlTextField.getText());
    try {
      sQLQueryBuilderPane1.setConnection(sqlConnection);
      sQLQueryBuilderPane1.load();
    }

    catch (java.beans.PropertyVetoException e) {
      e.printStackTrace(System.err);
    }
  }//GEN-LAST:event_connectButtonActionPerformed

  private void sunJDBCRadioButton4ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunJDBCRadioButton4ActionPerformed
    // Add your handling code here:
    useCustomDriver = false;
    QCJdbcURL url = new QCJdbcURL(urlTextField.getText());
    url.protocol = "jdbc";
    url.driver = "odbc";
    urlTextField.setText(url.getURL());
  }//GEN-LAST:event_sunJDBCRadioButton4ActionPerformed

  private void postgresqlRadioButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postgresqlRadioButtonActionPerformed
    // Add your handling code here:
    useCustomDriver = false;
    QCJdbcURL url = new QCJdbcURL(urlTextField.getText());
    url.protocol = "jdbc";
    url.driver = "postgresql";
    urlTextField.setText(url.getURL());
  }//GEN-LAST:event_postgresqlRadioButtonActionPerformed

  private void as400RadioButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_as400RadioButtonActionPerformed
    // Add your handling code here:
    useCustomDriver = false;
    QCJdbcURL url = new QCJdbcURL(urlTextField.getText());
    url.protocol = "jdbc";
    url.driver = "as400";
    urlTextField.setText(url.getURL());
  }//GEN-LAST:event_as400RadioButtonActionPerformed

  private void customRadioButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRadioButtonActionPerformed
    // Add your handling code here:
    useCustomDriver = true;
    QCJdbcURL url = new QCJdbcURL(urlTextField.getText());
    url.protocol = "jdbc";
    url.driver = "PLEASE_EDIT_THIS_DRIVER_URL";
    urlTextField.setText(url.getURL());
  }//GEN-LAST:event_customRadioButtonActionPerformed

  // Make sure that drivers get loaded.
  private void instanceDriver(String urlDriver)
  throws java.sql.SQLException, java.lang.ClassNotFoundException {

    java.lang.Class driverClass;

    if (useCustomDriver) { // Assumes static ctor,
      // modify if you have to explicitly registerDriver()
      driverClass = Class.forName(driverClassNameTextField.getText());
      if (!classesInstanced.contains(driverClass)) {
        classesInstanced.add(driverClass);
      }
    }

    else if (urlDriver.equals("as400")) {
      driverClass = Class.forName("com.ibm.as400.access.AS400JDBCDriver");
      if (!classesInstanced.contains(driverClass)) {
        java.sql.DriverManager.registerDriver (new com.ibm.as400.access.AS400JDBCDriver());
        classesInstanced.add(driverClass);
      }
    }

    else if (urlDriver.equals("jdbc")) {
      driverClass = Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      if (!classesInstanced.contains(driverClass)) {
        classesInstanced.add(driverClass);
      }
    }

    else if (urlDriver.equals("postgresql")) {
      driverClass = Class.forName("postgresql.Driver");
      if (!classesInstanced.contains(driverClass)) {
        classesInstanced.add(driverClass);
      }
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel sessionPanel;
  private javax.swing.JPanel urlPanel;
  private javax.swing.JTextField urlTextField;
  private javax.swing.JButton connectButton;
  private javax.swing.JPanel driverTypePanel;
  private javax.swing.JRadioButton as400RadioButton;
  private javax.swing.JRadioButton sunJDBCRadioButton4;
  private javax.swing.JRadioButton postgresqlRadioButton;
  private javax.swing.JRadioButton customRadioButton;
  private javax.swing.JTextField driverClassNameTextField;
  private com.ibm.as400.vaccess.SQLQueryBuilderPane sQLQueryBuilderPane1;
  private javax.swing.JPanel queryButtonPanel;
  private javax.swing.JButton queryToTableButton;
  private javax.swing.JButton queryToFormButton;
  // End of variables declaration//GEN-END:variables

  private void establishButtonGroup() {
    buttonGroup = new javax.swing.ButtonGroup();
    buttonGroup.add(as400RadioButton);
    buttonGroup.add(sunJDBCRadioButton4);
    buttonGroup.add(postgresqlRadioButton);
    buttonGroup.add(customRadioButton);
  }

  /** Set the SQLResultSetTable in which the query will be executed
   * and results displayed when user presses the appropriate button.
   * @param tp A QCSQLResultsSetTablePane.
   */
  public void setResultTable(QCSQLResultSetTablePanel tp) {
    resultTable = tp;
  }

  /** Set the SQLResultSetForm in which the query will be executed
   * and results displayed when user presses the appropriate button.
   * @param fp A QCSQLResultSetForm
   */
  public void setResultForm(QCSQLResultSetFormPanel fp) {
    resultForm = fp;
  }

  /** Connects the ErrorDialogAdapter with any as400.vaccess components present.
   * @param eda An instance of an ErrorDialogAdapter already associated
   * with a suitable Frame.
   */
  public void propagateEDA(com.ibm.as400.vaccess.ErrorDialogAdapter eda) {
    errorDialogAdapter=eda;
    sQLQueryBuilderPane1.addErrorListener(eda);
  }
}
