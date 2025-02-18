package showAllAttributes;

import Classes.Student;
import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public interface showAll {
    default void initialize(){};

    default int userExists(){
        return 0;
    }

    default boolean subjectValid(){
        return false;
    }

    default void setStudent(Student student) {}

    default void transaction(Text text, int second) {}

    default void transaction(ImageView load, int second) {}
}
