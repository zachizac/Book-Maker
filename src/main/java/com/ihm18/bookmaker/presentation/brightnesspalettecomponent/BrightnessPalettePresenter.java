package com.ihm18.bookmaker.presentation.brightnesspalettecomponent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.SepiaTone;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;

import com.ihm18.bookmaker.presentation.editionactionscomponent.EditionActionsModel;

/**
 *
 * @author oualidqannouf
 */
public class BrightnessPalettePresenter implements Initializable {


	@Inject
	private EditionActionsModel editionActionsModel;
	@FXML
	private Slider brightnessSlider;
	
	public void launch() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		brightnessSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                        setImageEffect();
                }
            });
	}

	private void setImageEffect() {
		 System.out.println("hello");
	        ColorAdjust colorAdjust = new ColorAdjust();
	        colorAdjust.setBrightness((1 - brightnessSlider.valueProperty().get()) * -1);

	        SepiaTone st = new SepiaTone();

	        colorAdjust.setInput(st);
	        System.out.println("heeeey");
	        editionActionsModel.getImageView().setEffect(colorAdjust);
	    }


}
