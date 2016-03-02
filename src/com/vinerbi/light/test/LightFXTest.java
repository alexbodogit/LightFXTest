/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinerbi.light.test;

import java.awt.Point;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Alessio
 */
public class LightFXTest extends Application
{

    private Scene scene = null;

    private Group groupCircles;

    private Random rand;

    private Group group = null;

    private Group groupBackground = null;

    private int nCircles = 2000;
    private Point[] offsets = new Point[nCircles];

    private float angle = 0;

    Light.Point light;

    private void makeCircles()
    {
        groupCircles = new Group();
        groupBackground = new Group();

        rand = new Random();

        Rectangle rect = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]
                        {
                            new Stop(0, Color.RED),
                            new Stop(0.14, Color.RED),
                            new Stop(0.28, Color.PINK),
                            new Stop(0.43, Color.YELLOW),
                            new Stop(0.57, Color.GRAY),
                            new Stop(0.71, Color.GREEN),
                            new Stop(0.85, Color.BROWN),
                            new Stop(1, Color.ORANGE),
                }));

        groupBackground.getChildren().add(rect);
       
        for (int i = 0; i < nCircles; i++)
        {
            offsets[i] = new Point(rand.nextInt(3), rand.nextInt(3));

            Circle circle = new Circle(rand.nextInt(90), Color.web("white", 0.2));

            circle.setTranslateX(rand.nextInt((int) scene.getWidth()));
            circle.setTranslateY(rand.nextInt((int) scene.getHeight()));
            circle.setTranslateZ(rand.nextInt(120));
            groupCircles.getChildren().add(circle);
        }

        Group totalGroup = new Group(new Rectangle(scene.getWidth(), scene.getHeight(), Color.BLACK), groupCircles);

        groupCircles.setRotationAxis(new Point3D(0,0,1));
        groupCircles.setDepthTest(DepthTest.ENABLE);
        light = new Light.Point();
        light.setX(100);
        light.setY(100);

        light.setZ(50);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(45.0);
         
        
        groupCircles.setEffect(lighting);
        groupBackground.setBlendMode(BlendMode.COLOR_BURN);
        totalGroup.getChildren().add(groupBackground);

        group.getChildren().add(totalGroup);
         
        PointLight light = new PointLight();
        light.setColor(Color.RED);
        light.setTranslateZ(200);

        groupCircles.getChildren().add(light);

        AnimationTimer t = new AnimationTimer()
        {

            @Override
            public void handle(long now)
            {
                updateCircle();
            }
        };

        t.start();
        
         
    }

    private void updateCircle()
    {
        for (int i = 0; i < nCircles; i++)
        {
            Circle c = (Circle) groupCircles.getChildren().get(i);
            if (c.getTranslateX() <= 0 || c.getTranslateX() + c.getRadius() >= scene.getWidth())
            {
                offsets[i].x = -offsets[i].x;
            }

            if (c.getTranslateY() <= 0 || c.getTranslateY() + c.getRadius() >= scene.getHeight())
            {
                offsets[i].y = -offsets[i].y;
            }

            c.setTranslateX(c.getTranslateX()+ offsets[i].x);
            c.setTranslateY(c.getTranslateY() + offsets[i].y);

        }

        

    }

    @Override
    public void start(Stage primaryStage)
    {

        group = new Group();
        scene = new Scene(group, 1920, 1280);

        scene.setOnMouseMoved(event ->
        {
            light.setX(event.getSceneX());
            light.setY(event.getScreenY());

        });

        PerspectiveCamera camera = new PerspectiveCamera();

        scene.setCamera(camera);

        makeCircles();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
