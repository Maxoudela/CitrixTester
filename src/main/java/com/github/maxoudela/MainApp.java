/**
 * MIT License
 *
 * Copyright (c) 2020 Samir Hadzic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.maxoudela;

import java.nio.ByteBuffer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private TableView<ClipBoardEntry> tableView;

    @Override
    public void start(Stage stage) throws Exception {
        // Display the ClipBoard content according to Java API each time the button is pressed.
        Button pasteButton = new Button("Paste from clipboard");
        pasteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pasteClipboard();
            }
        });

        tableView = new TableView<>();
        TableColumn<ClipBoardEntry, String> typeName = new TableColumn<>("Format name");
        typeName.setCellValueFactory(new PropertyValueFactory<ClipBoardEntry, String>("formatType"));
        typeName.setPrefWidth(100);
        TableColumn<ClipBoardEntry, String> contentType = new TableColumn<>("Content type");
        contentType.setCellValueFactory(new PropertyValueFactory<ClipBoardEntry, String>("contentType"));
        contentType.setPrefWidth(100);
        TableColumn<ClipBoardEntry, String> content = new TableColumn<>("Content");
        content.setCellValueFactory(new PropertyValueFactory<ClipBoardEntry, String>("content"));
        tableView.getColumns().addAll(typeName, contentType, content);
        tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);

        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox vbox = new VBox(pasteButton, tableView);
        vbox.setSpacing(15);
        Scene scene = new Scene(vbox);

        stage.setTitle("Citrix Tester");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Holds one clipboard entry.
     */
    public class ClipBoardEntry {

        // The format name
        private StringProperty formatType = new SimpleStringProperty();
        // The content in a string if possible
        private StringProperty content = new SimpleStringProperty();
        // Whether the content is handled correctly
        private StringProperty contentType = new SimpleStringProperty();

        public ClipBoardEntry(String format, String contentType, String content) {
            this.formatType.set(format);
            this.content.set(content);
            this.contentType.set(contentType);
        }

        public StringProperty formatTypeProperty() {
            return formatType;
        }

        public StringProperty contentProperty() {
            return content;
        }

        public StringProperty contentTypeProperty() {
            return contentType;
        }
    }

    /**
     * Retrieve all the entries from the system ClipBoard and fill the TableView
     * with them.
     */
    private void pasteClipboard() {
        // Retrieve the system clipboard using Java API
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        ObservableList<ClipBoardEntry> items = FXCollections.observableArrayList();
        
        // Run trough each entries
        for (DataFormat dataformat : clipboard.getContentTypes()) {
            Object content = clipboard.getContent(dataformat);

            String theContent;
            String contentType;
            if (content instanceof String) {
                theContent = (String) content;
                contentType = "String";
            } else if (content instanceof ByteBuffer) {
                theContent = new String(((ByteBuffer) content).array());
                contentType = "ByteBuffer";
            } else {
                theContent = content.toString();
                contentType = "Unknown: " + content.getClass();
            }
            items.add(new ClipBoardEntry(dataformat.toString(), contentType, theContent));
        }
        // Clear and fill the TableView again.
        tableView.setItems(items);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
