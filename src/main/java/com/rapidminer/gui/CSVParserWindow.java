package com.rapidminer.gui;

import com.rapidminer.service.CSVParserService;
import com.rapidminer.service.CSVProcessingService;
import com.rapidminer.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

import static com.rapidminer.util.ImageUtil.createImageIcon;

public class CSVParserWindow extends JFrame {

    private static final String DEFAULT_WS_URL = "http://localhost:8080/median";
    private final JTable csvDataTable;
    private final JLabel statusIcon;
    private final JTextField selectedFileName;
    private final JTextField wsURLField;
    private Map<String, List<String>> parsedData;
    private JButton parseButton;
    private JButton wsCallButton;
    private File selectedFile;

    public CSVParserWindow() throws HeadlessException {
        super("CSV Parser");

        /**
         * init gui
         */
        setSize(800, 640);
        setMinimumSize(new Dimension(780, 640));
        setLayout(new BorderLayout());

        // action panel
        JPanel actionPanel = new JPanel();

        // upload file
        JButton uploadFile = createUploadFileButton();
        uploadFile.setIcon(createImageIcon("/images/upload_icon.png"));

        actionPanel.add(uploadFile);

        // end - action panel

        // show selected file name
        JLabel selectedFileLabel = new JLabel("File:");

        selectedFileName = new JTextField(15);
        selectedFileName.setPreferredSize(new Dimension(100, 40));
        selectedFileName.setEditable(false);


        actionPanel.add(selectedFileLabel);
        actionPanel.add(selectedFileName);

        // parse button
        parseButton = createParseButton();
        actionPanel.add(parseButton);

        // add separator
        JSeparator wsSeparator = new JSeparator(SwingConstants.VERTICAL);
        Dimension wsSeparatorDimension = wsSeparator.getPreferredSize();
        wsSeparatorDimension.height = actionPanel.getPreferredSize().height;
        wsSeparator.setPreferredSize(wsSeparatorDimension);
        actionPanel.add(wsSeparator);

        JLabel urlLabel = new JLabel("Url:");
        actionPanel.add(urlLabel);

        wsURLField = new JTextField(18);
        wsURLField.setText(DEFAULT_WS_URL);
        wsURLField.setPreferredSize(new Dimension(100, 40));
        actionPanel.add(wsURLField);

        // call WS button
        JButton callWSButton = createWSCallButton();
        callWSButton.setPreferredSize(new Dimension(100, 40));
        actionPanel.add(callWSButton);

        // step status
        statusIcon = new JLabel();
        statusIcon.setIcon(ImageUtil.createImageIcon(FileStatus.INIT.getImage()));

        actionPanel.add(statusIcon);

        // add action panel to the frame
        getContentPane().add(actionPanel, BorderLayout.NORTH);


        // init empty table
        this.csvDataTable = new JTable(new DefaultTableModel());
        JScrollPane tableScrollPanel = new JScrollPane(csvDataTable);

        JPanel csvDataTableContainer = new JPanel();
        csvDataTableContainer.setLayout(new BorderLayout());
        csvDataTableContainer.setBorder(new EmptyBorder(3, 3, 3, 3));

        csvDataTableContainer.add(tableScrollPanel);

        getContentPane().add(csvDataTableContainer, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(100, 100);
        pack();
        setVisible(true);

    }

    private JButton createUploadFileButton() {
        JButton uploadFile = new JButton("Select file");
        uploadFile.setPreferredSize(new Dimension(100, 40));

        uploadFile.addActionListener(event -> {
            JFileChooser openCSVFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            FileNameExtensionFilter filter = new FileNameExtensionFilter("csv files (*.csv)", "csv");
            openCSVFile.setFileFilter(filter);
            int ret = openCSVFile.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {

                selectedFile = openCSVFile.getSelectedFile();

                selectedFileName.setText(selectedFile.getName());
                parseButton.setEnabled(true);
                wsCallButton.setEnabled(false);
                statusIcon.setIcon(createImageIcon(FileStatus.READY_FOR_PARSE.getImage()));
            }
        });

        return uploadFile;
    }

    private JButton createParseButton() {
        parseButton = new JButton("Parse file");
        parseButton.setPreferredSize(new Dimension(100, 40));
        parseButton.setEnabled(false);
        parseButton.setIcon(createImageIcon("/images/parse_icon.png"));

        parseButton.addActionListener(e ->

                // consumer for csv parsed data
                // refresh table with returned data
                CSVParserService.parseAndConsume(selectedFile, (result) -> {
                    csvDataTable.setModel(new CSVTableModel(result));
                    parsedData = result;

                    parseButton.setEnabled(false);
                    wsCallButton.setEnabled(true);
                    statusIcon.setIcon(createImageIcon(FileStatus.READY_FOR_WS.getImage()));
                }));

        return parseButton;
    }

    private JButton createWSCallButton() {
        wsCallButton = new JButton("Call WS");
        wsCallButton.setEnabled(false);
        wsCallButton.setPreferredSize(new Dimension(100, 40));
        wsCallButton.setIcon(createImageIcon("/images/call_ws_icon.png"));

        wsCallButton.addActionListener(e -> {
            statusIcon.setIcon(createImageIcon(FileStatus.IN_PROGRESS.getImage()));
            CSVProcessingService.calculateMedian(parsedData, wsURLField.getText(),
                    () -> {
                        statusIcon.setIcon(createImageIcon(FileStatus.PROCESSED.getImage()));
                        statusIcon.setToolTipText("File is exported");
                    },
                    (error) -> {
                        statusIcon.setIcon(createImageIcon(FileStatus.ERROR.getImage()));
                        System.err.println(error.getMessage());
                        return null;
                    }
            );
        });

        return wsCallButton;
    }
}
