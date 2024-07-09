package helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class MessageDialogHelpers {

    /**
     * show 1 message dialog thông báo cho người dùng
     * 
     * @param parent component nơi hiển thị thông báo
     * @param title tiêu đề thông báo
     * @param content nội dung thông báo
     * 
     */
    public static void showMessageDialog(Window parent, String title, String content) {
       Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(parent);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * show 1 message dialog thông báo lỗi cho người dùng
     * 
     * @param parent component nơi hiển thị thông báo
     * @param title tiêu đề thông báo
     * @param content nội dung thông báo
     * 
     */
    public static void showErrorDialog(Window parent, String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
    alert.initOwner(parent);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
    }

    /**
     * show 1 confirm dialog hỏi người dùng xác nhận
     * 
     * @param parent component nơi hiển thị thông báo
     * @param title tiêu đề thông báo
     * @param content nội dung thông báo
     * 
     */
    public static int showConfirmDialog(Window parent, String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(parent);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        if (result == ButtonType.YES) {
            return 0; // YES_OPTION
        } else {
            return 1; // NO_OPTION
        }
    }
    
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
