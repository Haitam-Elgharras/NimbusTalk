package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Server.dao.entities.User;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerTest extends ApplicationTest {
    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = new Controller();
        controller.userName = new TextField();
        controller.passWord = new TextField();
        controller.regName = new TextField();
        controller.regPass = new TextField();
        controller.regEmail = new TextField();
        controller.regFirstName = new TextField();
        controller.regPhoneNo = new TextField();
        controller.male = new RadioButton();
        controller.controlRegLabel = new Label();
        controller.checkEmail = new Label();
        controller.nameExists = new Label();
        controller.btnBack = new ImageView();
        controller.pnSignIn = new Pane();

        // Clear the users list before each test
        Controller.users.clear();
    }

    @Test
    void checkUser() {
        // Add a user to the users list
        User testUser = new User();
        testUser.setUsername("testUser");
        Controller.users.add(testUser);

        // Test with a username that exists
        assertFalse(controller.checkUser("testUser"));

        // Test with a username that does not exist
        assertTrue(controller.checkUser("nonExistentUser"));
    }

    @Test
    void checkEmail() {
        // Add a user to the users list
        User testUser = new User();
        testUser.setEmail("testEmail");
        Controller.users.add(testUser);

        // Test with an email that exists
        assertFalse(controller.checkEmail("testEmail"));

        // Test with an email that does not exist
        assertTrue(controller.checkEmail("nonExistentEmail"));
    }

    @Test
    void makeDefault() {
        // Set some values to the fields
        controller.regName.setText("testName");
        controller.regPass.setText("testPass");
        controller.regEmail.setText("testEmail");
        controller.regFirstName.setText("testFirstName");
        controller.regPhoneNo.setText("testPhoneNo");
        controller.male.setSelected(false);

        // Call makeDefault method
        controller.makeDefault();

        // Assert that the fields are cleared
        assertEquals("", controller.regName.getText());
        assertEquals("", controller.regPass.getText());
        assertEquals("", controller.regEmail.getText());
        assertEquals("", controller.regFirstName.getText());
        assertEquals("", controller.regPhoneNo.getText());
        assertTrue(controller.male.isSelected());
    }


    @Test
    void handleMouseEvent() {
        // Create a mock MouseEvent
        MouseEvent mockEvent = mock(MouseEvent.class);

        // Set the source of the event to be btnBack
        when(mockEvent.getSource()).thenReturn(controller.btnBack);

        // Call handleMouseEvent method
        controller.handleMouseEvent(mockEvent);

        // Assert that the expected changes have occurred
        // This will depend on what handleMouseEvent is supposed to do
        // For example, if it's supposed to change the opacity of pnSignIn:
        assertEquals(1, controller.pnSignIn.getOpacity());
    }
}