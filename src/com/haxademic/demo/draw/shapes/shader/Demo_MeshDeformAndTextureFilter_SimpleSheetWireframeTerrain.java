package com.haxademic.demo.draw.shapes.shader;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.filters.pshader.VignetteFilter;
import com.haxademic.core.draw.image.SimplexNoiseTexture;
import com.haxademic.core.draw.shapes.Shapes;
import com.haxademic.core.draw.shapes.pshader.MeshDeformAndTextureFilter;
import com.haxademic.core.math.easing.LinearFloat;
import com.haxademic.core.math.easing.Penner;

import processing.core.PGraphics;
import processing.core.PShape;

public class Demo_MeshDeformAndTextureFilter_SimpleSheetWireframeTerrain 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected float cols = 25;
	protected float rows = 25;

	protected PShape shape;
	protected PGraphics texture;
	protected SimplexNoiseTexture displaceTexture;

	protected int FRAMES = 360;
	protected LinearFloat displaceAmpMult = new LinearFloat(0, 0.015f); 

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.LOOP_FRAMES, FRAMES);
		p.appConfig.setProperty(AppSettings.RENDERING_MOVIE, false );
		p.appConfig.setProperty(AppSettings.RENDERING_MOVIE_START_FRAME, 1 + FRAMES * 2);
		p.appConfig.setProperty(AppSettings.RENDERING_MOVIE_STOP_FRAME, 1 + FRAMES * 3);
	}

	protected void setupFirstFrame() {
		int sheetW = P.round(cols * 80);
		int sheetH = P.round(cols * 80);
		
		// displace texture
		displaceTexture = new SimplexNoiseTexture(sheetW, sheetH);
		
		// create wireframe texture
		texture = p.createGraphics(sheetW, sheetH, P.P2D);
		
		// build sheet mesh
		shape = Shapes.createSheet((int) cols, (int) rows, texture);
		
		// debug view
		p.debugView.setValue("shape.getVertexCount();", shape.getVertexCount());
		p.debugView.setTexture(texture);
		p.debugView.setTexture(displaceTexture.texture());
	}

	public void drawApp() {
		// ease amplitude
		if(p.loop.loopCurFrame() == 2) displaceAmpMult.setTarget(1);
		if(p.loop.loopCurFrame() == FRAMES - 80) displaceAmpMult.setTarget(0);
		displaceAmpMult.update();
		float easedAmp = Penner.easeInOutQuad(displaceAmpMult.value(), 0, 1, 1);
		
		// update texture
		texture.beginDraw();
		texture.background(0);
		texture.noFill();
		texture.stroke(255);
		texture.strokeWeight(3f);
		float cellW = (float) texture.width / cols;
		float cellH = (float) (texture.height) / rows;
		for (int x = 0; x <= texture.width; x += cellW) {
			float curX = P.min(x, texture.width - 1);
			texture.line(curX, 0, curX, texture.height);
		}
		for (int y = 0; y <= texture.height; y += cellH) {
			float curY = P.min(y, texture.height - 1);
			texture.line(0, curY, texture.height, curY);
		}
		
		// fade out
		texture.noStroke();
		for (int i = 0; i < 3; i++) {
			texture.beginShape();
			texture.fill(0);
			texture.vertex(0, 0);
			texture.vertex(texture.width, 0);
			texture.fill(0,0);
			texture.vertex(texture.width, texture.height * 0.65f);
			texture.vertex(0, texture.height * 0.65f);
			texture.endShape();
		}
		
		texture.endDraw();
		
		// context & camera
		background(0);
		//p.image(texture, 0, 0);
		DrawUtil.setCenterScreen(p.g);
		// DrawUtil.basicCameraFromMouse(p.g);
		p.g.rotateX(1.1f);

		// deform mesh
		displaceTexture.offsetY(p.frameCount * 0.01f);
		displaceTexture.zoom(5f);
		MeshDeformAndTextureFilter.instance(p).setDisplacementMap(displaceTexture.texture());
		MeshDeformAndTextureFilter.instance(p).setDisplaceAmp(easedAmp * 190f);
		MeshDeformAndTextureFilter.instance(p).setSheetMode(true);
		MeshDeformAndTextureFilter.instance(p).applyTo(p);

		// draw mesh
		p.shape(shape);
		p.resetShader();
		

		// post effects
//		BloomFilter.instance(p).setStrength(2.11f);
//		BloomFilter.instance(p).setBlurIterations(1);
//		BloomFilter.instance(p).setBlendMode(BloomFilter.BLEND_SCREEN);
//		BloomFilter.instance(p).applyTo(p);
//		BloomFilter.instance(p).applyTo(pg);
		
		VignetteFilter.instance(p).setDarkness(0.49f);
		VignetteFilter.instance(p).applyTo(p);

//		GrainFilter.instance(p).setTime(p.frameCount * 0.01f);
//		GrainFilter.instance(p).setCrossfade(0.12f);
//		GrainFilter.instance(p).applyTo(p);

	}
		
}