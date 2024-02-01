package tech.octopusdragon.intervaltimer.application.controls;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;

/**
 * A pie chart graphic that displays the elapsed time of a timer
 * @author Alex Gill
 *
 */
public class CountdownPieChart extends Pane {
	
	// --- Constants ---
	private static final double PIE_CHART_PROPORTION = 0.9;
	private static final double OUTLINE_PROPORTION = 0.05;
	
	// --- UI components ---
	@FXML private Arc pieChart;
	@FXML private Ellipse outline;
	
	// --- Stylable Properties ---
	// The width and height of the graphic
	private StyleableProperty<Number> sizeProperty;
	// The paint of the graphic
	private StyleableProperty<Paint> paintProperty;
	
	private static final StyleablePropertyFactory<CountdownPieChart> FACTORY =
			new StyleablePropertyFactory<>(Pane.getClassCssMetaData());
	private static final CssMetaData<CountdownPieChart, Number> SIZE =
			FACTORY.createSizeCssMetaData("-size", s -> s.sizeProperty);
	private static final CssMetaData<CountdownPieChart, Paint> PAINT =
			FACTORY.createPaintCssMetaData("-paint", s -> s.paintProperty);
	
	
	/**
	 * Constructs an empty countdown pie chart
	 */
	public CountdownPieChart() {
		
		// Initialize properties
		sizeProperty = new SimpleStyleableDoubleProperty(SIZE);
		paintProperty = new SimpleStyleableObjectProperty<Paint>(PAINT);
		sizeProperty().addListener((obs, oldVal, newVal) -> {
			setPrefWidth(newVal.doubleValue());
			setPrefHeight(newVal.doubleValue());
		});
		paintProperty().addListener((obs, oldVal, newVal) -> {
			pieChart.setFill(newVal);
			outline.setStroke(newVal);
		});
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CountdownPieChart.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays the elapsed time on the pie chart
	 * @param timeLeft The current time in the interval
	 * @param curDuration The total duration of the current interval
	 */
	public void displayElapsedTime(Duration timeLeft, Duration curDuration) {
		double lengthProportion = (double)timeLeft.toMillis() / curDuration.toMillis();
		if (lengthProportion < 0) lengthProportion = 0;
		double length = 360.0 * lengthProportion;
		pieChart.setLength(length);
	}
	

	
	/**
	 * @return the size
	 */
	public double getSize() {
		return sizeProperty.getValue().doubleValue();
	}
	

	/**
	 * sets the size
	 * @param size the value to set the size to
	 */
	public void setSize(double size) {
		sizeProperty.setValue(size);
	}
	

	/**
	 * @return the size property
	 */
	public ObservableDoubleValue sizeProperty() {
		return (ObservableDoubleValue)sizeProperty;
	}
	
	
	/**
	 * @return the paint
	 */
	public Paint getPaint() {
		return paintProperty.getValue();
	}
	
	/**
	 * sets the paint
	 * @param paint the value to set the paint to
	 */
	public void setPaint(Paint paint) {
		paintProperty.setValue(paint);
	}
	
	/**
	 * @return the paint property
	 */
	@SuppressWarnings("unchecked")
	public ObservableValue<Paint> paintProperty() {
		return (ObservableValue<Paint>)paintProperty;
	}
	
	
	
	@Override
	public void layoutChildren() {
		double width = Math.max(getMinWidth(), getPrefWidth());
		double height = Math.max(getMinHeight(), getPrefHeight());
		double size = width < height ? width : height;
		
		// Resize pie chart
		pieChart.setRadiusX(size * PIE_CHART_PROPORTION / 2);
		pieChart.setRadiusY(size * PIE_CHART_PROPORTION / 2);
		
		// Center pie chart
		pieChart.setCenterX(width / 2);
		pieChart.setCenterY(height / 2);
		
		// Resize outline
		outline.setRadiusX(size / 2);
		outline.setRadiusY(size / 2);
		outline.setStrokeWidth(size * OUTLINE_PROPORTION / 2);
		
		// Center pie chart
		outline.setCenterX(width / 2);
		outline.setCenterY(height / 2);
	}
	
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
	    return FACTORY.getCssMetaData();
	}
	
}
