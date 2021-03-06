/*
 * QCSpooledFileViewerPanel.java
 *
 * Created on July 18, 2000, 9:26 PM
 *
 * This is free open source software distributed under the IBM Public License found
 * on the World Wide Web at http://oss.software.ibm.com/developerworks/opensource/license10.html
 * Copyright *C* 2000, Jack J. Woehr, PO Box 51, Golden, CO 80402-0051 USA jax@well.com
 * Copyright *C* 2000, International Business Machines Corporation and others. All Rights Reserved.
 */

package com.SoftWoehr.JTOpenContrib.QCDemo;

/** Panel to hold a spool file viewer pane.
 * @author jax
 * @version 1.0
 */
public class QCSpooledFileViewerPanel extends javax.swing.JPanel implements QCServiceClient {

  private QCMgr manager;
  private java.lang.String serverName;
  private com.SoftWoehr.JTOpenContrib.QCDemo.QCServiceRecord serviceRecord;
  private com.ibm.as400.vaccess.ErrorDialogAdapter errorDialogAdapter;

  /** Creates new form QCSpooledFileViewerPanel */
  public QCSpooledFileViewerPanel() {
    initComponents ();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents () {//GEN-BEGIN:initComponents
    spooledFileViewer1 = new com.ibm.as400.vaccess.SpooledFileViewer ();
    jPanel1 = new javax.swing.JPanel ();
    systemTextField = new javax.swing.JTextField ();
    nameTextField = new javax.swing.JTextField ();
    numberTextField = new javax.swing.JTextField ();
    systemLabel = new javax.swing.JLabel ();
    nameLabel = new javax.swing.JLabel ();
    numberLabel = new javax.swing.JLabel ();
    jobNameTextField = new javax.swing.JTextField ();
    jobUserTextField = new javax.swing.JTextField ();
    jobNumberTextField = new javax.swing.JTextField ();
    jobNameLabel = new javax.swing.JLabel ();
    jobUserLabel = new javax.swing.JLabel ();
    jobNumberLabel = new javax.swing.JLabel ();
    getSpoolFileButton = new javax.swing.JButton ();
    setLayout (new java.awt.BorderLayout ());



    add (spooledFileViewer1, java.awt.BorderLayout.CENTER);

    jPanel1.setLayout (new java.awt.GridLayout (4, 3));

      systemTextField.setToolTipText ("Enter the name of the system on which the spool file resides.");

      jPanel1.add (systemTextField);

      nameTextField.setToolTipText ("Enter the name of the spool file.");

      jPanel1.add (nameTextField);

      numberTextField.setToolTipText ("Enter the spool file number.");

      jPanel1.add (numberTextField);

      systemLabel.setText ("System");

      jPanel1.add (systemLabel);

      nameLabel.setText ("Name");

      jPanel1.add (nameLabel);

      numberLabel.setText ("Number");

      jPanel1.add (numberLabel);

      jobNameTextField.setToolTipText ("Enter the name of the job which produced the spool file.");

      jPanel1.add (jobNameTextField);

      jobUserTextField.setToolTipText ("Enter the username of the account which produced the spool file.");

      jPanel1.add (jobUserTextField);

      jobNumberTextField.setToolTipText ("Enter the number of the job which created the spool file.");

      jPanel1.add (jobNumberTextField);

      jobNameLabel.setText ("Job Name");

      jPanel1.add (jobNameLabel);

      jobUserLabel.setText ("Job User");

      jPanel1.add (jobUserLabel);

      jobNumberLabel.setText ("Job Number");

      jPanel1.add (jobNumberLabel);


    add (jPanel1, java.awt.BorderLayout.NORTH);

    getSpoolFileButton.setToolTipText ("Fill in the fields at the top of the window, then press this button to view the spool file.");
    getSpoolFileButton.setText ("View Spool File");
    getSpoolFileButton.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        getSpoolFileButtonActionPerformed (evt);
      }
    }
    );


    add (getSpoolFileButton, java.awt.BorderLayout.SOUTH);

  }//GEN-END:initComponents

  private void getSpoolFileButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getSpoolFileButtonActionPerformed
    // Add your handling code here:

    // Loading the directory can take some time!
    java.awt.Cursor currentCursor = jPanel1.getCursor();
    setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

    reinstanceServerFromTextEntry(); // Get our AS400 object.

    // Set the spooled file viewer to the loaded spooled file.
    com.ibm.as400.access.SpooledFile spooledFile =
    new com.ibm.as400.access.SpooledFile
    (serviceRecord.as400, nameTextField.getText(), new Integer(numberTextField.getText()).intValue(),
    jobNameTextField.getText(), jobUserTextField.getText(), jobNumberTextField.getText());

    try {
      spooledFileViewer1.setSpooledFile(spooledFile);
      spooledFileViewer1.load();
    }

    catch (java.beans.PropertyVetoException e) {
      e.printStackTrace();
    }
    catch (java.io.IOException e) {
      e.printStackTrace();
    }

    // The wait is over.
    setCursor(currentCursor);
  }//GEN-LAST:event_getSpoolFileButtonActionPerformed

  private synchronized void reinstanceServerFromTextEntry() {
    String newName = systemTextField.getText();
    if (null == serverName) {
      serverName = "";
    }
    if (null != newName) {
      if (!newName.equals("")) {
        if (!newName.equals(serverName)) {
          serverName = newName;
          if (null != serviceRecord) {

            try { // let go of the service from manager
              manager.freeService(serviceRecord);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
            serviceRecord = null;
          }
          try {
            serviceRecord = manager.getService("qcmgr:" + serverName + "/PRINT", this);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private com.ibm.as400.vaccess.SpooledFileViewer spooledFileViewer1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JTextField systemTextField;
  private javax.swing.JTextField nameTextField;
  private javax.swing.JTextField numberTextField;
  private javax.swing.JLabel systemLabel;
  private javax.swing.JLabel nameLabel;
  private javax.swing.JLabel numberLabel;
  private javax.swing.JTextField jobNameTextField;
  private javax.swing.JTextField jobUserTextField;
  private javax.swing.JTextField jobNumberTextField;
  private javax.swing.JLabel jobNameLabel;
  private javax.swing.JLabel jobUserLabel;
  private javax.swing.JLabel jobNumberLabel;
  private javax.swing.JButton getSpoolFileButton;
  // End of variables declaration//GEN-END:variables
  /** Sets the QCMgr object which will handle AS400 instances for this client.
   * @param mgr A QCMgr object.
   */
  public void setManager(QCMgr mgr) {
    manager = mgr;
  }

  /** Connects the ErrorDialogAdapter with any as400.vaccess components present.
   * @param eda An instance of an ErrorDialogAdapter already associated
   * with a suitable Frame.
   */
  public void propagateEDA(com.ibm.as400.vaccess.ErrorDialogAdapter eda) {
    errorDialogAdapter=eda;
    spooledFileViewer1.addErrorListener(eda);
  }
  /** Required implementantion of the QCServiceClient interface. Releases the
   * AS400 object provided by the server.
   * @param sr The service record which represents the AS400 to be
   * relinquished.
   */
  public void relinquish(QCServiceRecord sr) {
    serviceRecord = null;
  }
}
