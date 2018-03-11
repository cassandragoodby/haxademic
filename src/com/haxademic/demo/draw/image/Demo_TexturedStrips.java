package com.haxademic.demo.draw.image;

import java.util.ArrayList;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.constants.PRenderers;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.file.DemoAssets;
import com.haxademic.core.file.FileUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Movie;

public class Demo_TexturedStrips
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected Movie testMovie;
	protected PImage staticImg;
	protected PImage[] threads;
	protected PGraphics bwMap;
	protected boolean videoMap = false;
	protected ArrayList<Particle> particles;
	protected int numParticles = 100;
	protected boolean debug = false;

	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, 800 );
		p.appConfig.setProperty( AppSettings.HEIGHT, 800 );
	}
	
	public void setupFirstFrame() {
		// load movie
		if(videoMap) {
			testMovie = DemoAssets.movieKinectSilhouette();
			testMovie.jump(0);
			testMovie.loop();
		} else {
			staticImg = DemoAssets.squareTexture();
		}
		
		// load map & build buffer
		bwMap = p.createGraphics(p.width, p.height, PRenderers.P3D);
		
		// load thread textures
		threads = new PImage[] {
				DemoAssets.textureJupiter(),
				DemoAssets.textureNebula(),
				DemoAssets.squareTexture(),
		};
		
		// build particles
		particles = new ArrayList<Particle>();
		for (int i = 0; i < numParticles; i++) {
			particles.add(new Particle());
		}
	}

	
	public void drawApp() {
		background(0);
		
		// update map
		// draw movie
		if(videoMap) {
			if(testMovie.width > 10) {
				ImageUtil.cropFillCopyImage(testMovie.get(), bwMap, true);
			}
		} else {
			ImageUtil.cropFillCopyImage(staticImg, bwMap, true);
		}
		bwMap.loadPixels();
		
		// draw debug
		if(debug) {
			p.image(bwMap, 0, 0);
		}
		
		// draw particles
		p.stroke(255);
		p.strokeWeight(1);
		p.noFill();
		for (int i = 0; i < numParticles; i++) {
			particles.get(i).update(p.g);
		}
	}
	
	public void keyPressed() {
		super.keyPressed();
		if(p.key == 'd') debug = !debug;
	}
	
	public class Particle {
		
		protected float BORDER = 10;
		protected float TAIL_W = 2;
		protected float TAIL_LERP_BLACK = 0.9f;
		protected float TAIL_LERP_SLOW = 0.3f;
		protected float TURN_LERP_LOW = 2f;
		protected float TURN_LERP_HIGH = 5f;
		protected float SPEED_SLOW = 5f;
		protected PVector pos = new PVector();
		protected PVector tailEnd = new PVector();
		protected PVector[] tail = new PVector[50];
		protected EasingFloat easedDir = new EasingFloat(0, 4);
		protected EasingFloat easedSpeed = new EasingFloat(0, 4);
		protected EasingFloat tailLerp = new EasingFloat(0.8f, 8);
		protected float activeSpeed;
		protected boolean onBlack;
		protected PImage texture;
		
		public Particle() {
			reset();
		}
		
		protected void reset() {
			onBlack = true;
			resetSpeed();
			newStartLocation();
			resetTail();
			resetDirection();
			texture = threads[MathUtil.randRange(0, threads.length - 1)];
		}
		
		protected void resetSpeed() {
			activeSpeed = MathUtil.randRangeDecimal(12f, 25f);	
			easedSpeed.setCurrent(activeSpeed);
			easedSpeed.setTarget(activeSpeed);
		}
		
		protected void newStartLocation() {
			int randStartWall = MathUtil.randRange(0, 3);
			if(randStartWall == 0) {
				// top
				pos.x = MathUtil.randRangeDecimal(0, p.width);
				pos.y = -BORDER;
			} else if(randStartWall == 1) {
				// right
				pos.x = p.width + BORDER;
				pos.y = MathUtil.randRangeDecimal(0, p.height);
			} else if(randStartWall == 2) {
				// bottom
				pos.x = MathUtil.randRangeDecimal(0, p.width);
				pos.y = p.height + BORDER;
			} else if(randStartWall == 3) {
				// left
				pos.x = -BORDER;
				pos.y = MathUtil.randRangeDecimal(0, p.height);
			} 
		}
		
		protected void resetTail() {
			for (int i = 0; i < tail.length; i++) {
				if(tail[i] == null) tail[i] = new PVector();
				tail[i].set(pos);
			}
			tailEnd = tail[tail.length - 1];
		}
		
		protected void resetDirection() {
			float offsetX = MathUtil.randRangeDecimal(0, p.width/5f);
			if(MathUtil.randBoolean(p) == true) offsetX *= -1f;
			float offsetY = MathUtil.randRangeDecimal(0, p.width/5f);
			if(MathUtil.randBoolean(p) == true) offsetY *= -1f;
			
			float dirToCenter = MathUtil.getRadiansToTarget(pos.x, pos.y, p.width/2 + offsetX, p.height/2 + offsetY);
			easedDir.setCurrent(-dirToCenter);
			easedDir.setTarget(-dirToCenter);
			easedDir.setEaseFactor(MathUtil.randRangeDecimal(TURN_LERP_LOW, TURN_LERP_HIGH));
		}
		
		protected void update(PGraphics pg) {
			// update props
			easedSpeed.update(true);
			
			// update location
			pos.add(P.cos(easedDir.value()) * easedSpeed.value(), P.sin(easedDir.value()) * easedSpeed.value());
			tail[0].set(pos); // copy to top tail
			if(tailEnd.x < -BORDER || tailEnd.x > pg.width + BORDER || tailEnd.y < -BORDER || tailEnd.y > pg.height + BORDER) reset();
			
			// get pixel color
			int pixelColor = ImageUtil.getPixelColor(bwMap, (int) pos.x, (int) pos.y);
			float r = ColorUtil.redFromColorInt(pixelColor) / 255f;
			if(r < 0.1f && onBlack == false) {
				// on black!
				onBlack = true;
				float turnRads = P.PI * MathUtil.randRangeDecimal(0.9f, 1.1f); 
				if(MathUtil.randBoolean(p) == true) turnRads *= -1f;
				easedDir.setTarget(easedDir.target() +turnRads);
				tailLerp.setTarget(TAIL_LERP_BLACK);
				resetSpeed();
			} else if(r >= 0.1f && onBlack == true) {
				// on white!
				onBlack = false;
				easedSpeed.setTarget(SPEED_SLOW);
				tailLerp.setTarget(TAIL_LERP_SLOW);
			}

			// update extra params after updating position
			easedDir.update(true);
			tailLerp.update(true);

			// copy up tail
			for (int i = tail.length - 2; i >= 0; i--) {
				tail[i+1].lerp(tail[i], tailLerp.value());
			}
			
			// draw tail!
			// pg.point(pos.x, pos.y, pos.z);
			pg.fill(255);
			pg.noStroke();
			pg.beginShape(P.QUADS);
			pg.texture(texture);
			
			float textureY = 0;
			float segmentDir = easedDir.value();

			for (int i = 0; i < tail.length - 2; i++) {
				// get rect points
				float leftXCur = tail[i].x + TAIL_W * P.cos(segmentDir - P.HALF_PI);
				float leftYCur = tail[i].y + TAIL_W * P.sin(segmentDir - P.HALF_PI);
				float rightXCur = tail[i].x + TAIL_W * P.cos(segmentDir + P.HALF_PI);
				float rightYCur = tail[i].y + TAIL_W * P.sin(segmentDir + P.HALF_PI);
				
				segmentDir = -MathUtil.getRadiansToTarget(tail[i].x, tail[i].y, tail[i+1].x, tail[i+1].y);
				float leftXNext = tail[i+1].x + TAIL_W * P.cos(segmentDir - P.HALF_PI);
				float leftYNext = tail[i+1].y + TAIL_W * P.sin(segmentDir - P.HALF_PI);
				float rightXNext = tail[i+1].x + TAIL_W * P.cos(segmentDir + P.HALF_PI);
				float rightYNext = tail[i+1].y + TAIL_W * P.sin(segmentDir + P.HALF_PI);
				
				pg.vertex(leftXCur,  leftYCur,  			0, textureY);
				pg.vertex(rightXCur, rightYCur, 			texture.width, textureY);
				textureY += (float) texture.height / (float) tail.length;
				pg.vertex(rightXNext, rightYNext, 		texture.width, textureY);
				pg.vertex(leftXNext, leftYNext, 			0, textureY);

				
				// old test draw
//				pg.line(leftXCur, leftYCur, rightXCur, rightYCur);
//				pg.line(tail[i].x, tail[i].y, tail[i].z, tail[i+1].x, tail[i+1].y, tail[i+1].z);
			}
			pg.endShape();

		}
	}
}
