package uk.ac.imperial.flexibledemand.charts.saso;

import processing.core.*;
import static processing.core.PConstants.BOTTOM;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.HALF_PI;
import processing.core.PShapeSVG.Font;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

/**
 * {Insert class description here}
 *
 * @author Patricio E. Petruzzi
 * @version
 * @since
 *
 */
public class DrawArea extends PApplet {

    //Declaraciones iniciales aquí...
    SasoInterface app;
    // Used for oveall rotation
    float angle = 2f;
// Cube count-lower/raise to test performance
    int limit = 1, round=0;
// Array for all cubes
    Bar[] bars = new Bar[24];
    TProgressBar progress;
    PFont f = createFont("Georgia", 48);
    PFont f2 = createFont("Georgia", 30);
    PFont f3 = createFont("Georgia", 24);

    public DrawArea(SasoInterface ref) {
        this.app = ref;
    }

    @Override
    public void setup() {
        size(1440, 700, P3D);
        frameRate(4);
        progress = new TProgressBar(50, 300, 1275, 275);
        //progress.setHeightProgress(300);
        //noStroke();
        refresh();
    }

    public void refresh() {
        int[] slots = app.getBars();
        // Instantiate cubes, passing in random vals for size and postion
        for (int i = 0; i < bars.length; i++) {
            bars[i] = new Bar(100 + 45 * i, 100, 0, i, slots[i]);
        }
        progress.setValue((float) app.getSatisfaction()*300);
        round = app.getRound();
    }

    @Override
    public void draw() {
        refresh();
        background(200);

        fill(50, 50, 50);
        textFont(f);
        textAlign(CENTER, CENTER);
        text("Self-Organising Flexible Demand for SmartGrids", 615, 50, 0);

        textFont(f2);
        textAlign(CENTER, BOTTOM);

        pushMatrix();
        translate(60, 350);
        rotate(-HALF_PI);
        text("Allocations", 0, 0);
        popMatrix();

        text("Time Slots", 625, 635);

        textFont(f3);
        textAlign(CENTER, CENTER);
        text("Average", 1300, 450);
        text("consumer", 1300, 475);
        text("satisfaction", 1300, 500);

        text("Round: "+round, 1300, 575);
        // Set up some different colored lights
        //pointLight(150, 200, 200, 0, 0, 1000);
        pointLight(150, 200, 200, 720, 350, 2000);

        // Raise overall light in scene 
        ambientLight(70, 70, 10);

        // Center geometry in display windwow.
        // you can changlee 3rd argument ('0')
        // to move block group closer(+) / further(-)
        //translate(0,0, (float) (-200+mouseX * 0.65));

        // Rotate around y and x axes
        //rotateY(radians(angle));
        //rotateX(radians(angle));

        // Draw cubes
        for (int i = 0; i < bars.length; i++) {
            bars[i].drawBar();
        }

        progress.draw();
        // Used in rotate function calls above
        //angle += 0.2;
        /*
         * for (int i = 0; i < app.getBars().length; i++) {
         * int ok = app.getBars()[i];
         * text("Bar " + i + ": " + ok, width * 0.15f, height * 0.5f + i * 10);
         * }
         * text("Average Consumer Satisfaction : " + app.getSatisfaction(), width * 0.15f,
         * height * 0.5f + 25 * 10);
         */
    }

    class Cube {
// Properties

        int w, h, d;
        int shiftX, shiftY, shiftZ;
        boolean correct;

        // Constructor
        Cube(int w, int h, int d, int shiftX, int shiftY, int shiftZ, boolean correct) {
            this.w = w;
            this.h = h;
            this.d = d;
            this.shiftX = shiftX;
            this.shiftY = shiftY;
            this.shiftZ = shiftZ;
            this.correct = correct;
        }

        // Main cube drawing method, which looks 
        // more confusing than it really is. It's 
        // just a bunch of rectangles drawn for 
        // each cube face
        public void drawCube() {
            if (correct) {
                fill(0, 255, 0);
            } else {
                fill(255, 0, 0);
            }
            beginShape(QUADS);
            // Front face
            vertex(-w / 2 + shiftX, -h / 2 + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, -h / 2 + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, h + shiftY, -d / 2 + shiftZ);
            vertex(-w / 2 + shiftX, h + shiftY, -d / 2 + shiftZ);

            // Back face
            /*vertex(-w / 2 + shiftX, -h / 2 + shiftY, d + shiftZ);
            vertex(w + shiftX, -h / 2 + shiftY, d + shiftZ);
            vertex(w + shiftX, h + shiftY, d + shiftZ);
            vertex(-w / 2 + shiftX, h + shiftY, d + shiftZ);

            // Left face
            vertex(-w / 2 + shiftX, -h / 2 + shiftY, -d / 2 + shiftZ);
            vertex(-w / 2 + shiftX, -h / 2 + shiftY, d + shiftZ);
            vertex(-w / 2 + shiftX, h + shiftY, d + shiftZ);
            vertex(-w / 2 + shiftX, h + shiftY, -d / 2 + shiftZ);

            // Right face
            vertex(w + shiftX, -h / 2 + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, -h / 2 + shiftY, d + shiftZ);
            vertex(w + shiftX, h + shiftY, d + shiftZ);
            vertex(w + shiftX, h + shiftY, -d / 2 + shiftZ);

            // Top face
            vertex(-w / 2 + shiftX, -h / 2 + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, -h / 2 + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, -h / 2 + shiftY, d + shiftZ);
            vertex(-w / 2 + shiftX, -h / 2 + shiftY, d + shiftZ);

            // Bottom face
            vertex(-w / 2 + shiftX, h + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, h + shiftY, -d / 2 + shiftZ);
            vertex(w + shiftX, h + shiftY, d + shiftZ);
            vertex(-w / 2 + shiftX, h + shiftY, d + shiftZ);
            */
            endShape();

            // Add some rotation to each box for pizazz.
            //rotateY(radians(1));
            //rotateX(radians(1));
            //rotateZ(radians(1));
        }
    }

    class Bar {
// Properties

        int x, y, z, number;
        int size = 15;
        int dev = 10;
        Cube[] cubes = new Cube[16];
        PFont f = createFont("Georgia", 18);
        // Constructor

        Bar(int x, int y, int z, int number, int ok) {
            super();
            this.x = x;
            this.y = y;
            this.z = z;
            this.number = number;
            // Instantiate cubes, passing in random vals for size and postion
            for (int i = 0; i < cubes.length; i++) {
                cubes[i] = new Cube(size, size, size, x, y + 2 * size * i, z, i + ok > cubes.length);
            }
        }

        // Main cube drawing method, which looks 
        // more confusing than it really is. It's 
        // just a bunch of rectangles drawn for 
        // each cube face
        public void drawBar() {
            for (int i = 0; i < cubes.length; i++) {
                cubes[i].drawCube();
            }
            fill(50, 50, 50);
            textFont(f);
            textAlign(CENTER, CENTER);
            if(number>9)
            text(number, x , y + 2 * size * (cubes.length), z);
            else
            text(number, x + dev, y + 2 * size * (cubes.length), z);    
        }
    }

    class TProgressBar {

        private int x;
        private int y;
        private int maxWidth;
        private int heightProgress;
        private float value;
        private PFont font;
        private int backgroundColor;
        private int progressColor;
        private int sizeFont;

        public TProgressBar(int maxWidth, int heightProgress, int x, int y) {

            this.maxWidth = maxWidth;

            this.heightProgress = heightProgress;

            this.x = x;

            this.y = y;

            this.value = x;

            this.backgroundColor = color(175,175,175);

            this.progressColor = color(0,200,0);

            this.sizeFont = 30;

            this.font = createFont("Georgia", 24);

            textFont(this.font);

            textSize(this.sizeFont);

            smooth();
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public void setHeightProgress(int heightProgress) {
            this.heightProgress = heightProgress;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public void setProgressColor(int progressColor) {
            this.progressColor = progressColor;
        }

        public void setFontSize(int fontSize) {
            this.sizeFont = sizeFont;
        }

        public int percentage() {
            return (int) ((this.value * 100) / heightProgress);
        }

        public void setValue(float value) {
            if (value <= this.heightProgress) {
                this.value = value;
            } else {
                println("valor es mayor que el limite(maxWidth) de la barra de progreso: "+value);
            }
        }

        public void draw() {
            pushStyle();
            
            textSize(this.sizeFont);

            strokeCap(SQUARE);

            stroke(this.backgroundColor);

            strokeWeight(heightProgress);

            line(this.x, this.y, this.x + this.maxWidth, this.y);

            stroke(this.progressColor);

            strokeWeight(maxWidth);

            line(this.x+maxWidth/2,this.y+heightProgress/2 , this.x+maxWidth/2 , this.y+heightProgress/2- this.value);

            textAlign(CENTER);

            text(percentage() + " %", this.x + this.maxWidth / 2, this.y - 175);

            popStyle();
        }
    }
}
