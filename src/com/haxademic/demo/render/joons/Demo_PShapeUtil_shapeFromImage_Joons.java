package com.haxademic.demo.render.joons;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.shapes.PShapeUtil;
import com.haxademic.core.file.DemoAssets;
import com.haxademic.core.render.JoonsWrapper;

import processing.core.PShape;

public class Demo_PShapeUtil_shapeFromImage_Joons 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected PShape shape;
	String[] materials = new String[] {
			JoonsWrapper.MATERIAL_PHONG,
			JoonsWrapper.MATERIAL_AMBIENT_OCCLUSION,
			JoonsWrapper.MATERIAL_GLASS,
			JoonsWrapper.MATERIAL_DIFFUSE,
			JoonsWrapper.MATERIAL_MIRROR,
			JoonsWrapper.MATERIAL_SHINY,
	};

	protected int FRAMES = 360;
	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, 1200 );
		p.appConfig.setProperty( AppSettings.HEIGHT, 900 );
		p.appConfig.setProperty( AppSettings.LOOP_FRAMES, FRAMES );
		p.appConfig.setProperty( AppSettings.SUNFLOW, true );
		p.appConfig.setProperty( AppSettings.SUNFLOW_ACTIVE, true );
		p.appConfig.setProperty( AppSettings.SUNFLOW_QUALITY, AppSettings.SUNFLOW_QUALITY_HIGH );
		p.appConfig.setProperty( AppSettings.RENDERING_IMAGE_SEQUENCE, true );
		p.appConfig.setProperty( AppSettings.RENDERING_IMAGE_SEQUENCE_START_FRAME, 3 );
		p.appConfig.setProperty( AppSettings.RENDERING_IMAGE_SEQUENCE_STOP_FRAME, 3 + FRAMES );
	}
	
	public void setupFirstFrame() {
		shape = PShapeUtil.shapeFromImage(DemoAssets.textureCursor());
		PShapeUtil.centerShape(shape);
		PShapeUtil.scaleVertices(shape, 1, 1, 4);
		PShapeUtil.scaleShapeToExtent(shape, p.height * 0.2f);
	}
	
	public void drawApp() {
		if(p.appConfig.getBoolean(AppSettings.SUNFLOW_ACTIVE, false) == false) {
			p.background(200, 255, 200);
//			DrawUtil.setCenterScreen(p);
			DrawUtil.setBetterLights(p);
			p.noStroke();
//			DrawUtil.basicCameraFromMouse(p.g);
		} else {
			joons.jr.background(JoonsWrapper.BACKGROUND_AO);
			joons.setUpRoom(255, 255, 255);
		}
		p.translate(0, -p.height * 0.1f, -p.width);
		p.rotateX(-0.2f);

		// draw shape
		float segmentRads = P.TWO_PI / materials.length;
		for (int i = 0; i < materials.length; i++) {
			float curRads = segmentRads * i - p.loop.progressRads();
			float radius = 400;
			p.pushMatrix();
			p.translate(P.cos(curRads) * radius, p.height * 0.1f * P.sin(segmentRads * i * 2f + p.loop.progressRads() * 2), P.sin(curRads) * radius);
//			p.rotateY(-curRads);
			p.rotateZ(P.HALF_PI);
			p.rotateX(-curRads + P.HALF_PI);
//		p.rotateY(0.3f + p.frameCount * 0.1f);
//			PShapeUtil.drawTriangles(p.g, shape, null, 1);
			PShapeUtil.drawTrianglesJoons(p, shape, 1, materials[i]);
			p.popMatrix();
		}
		
		// draw floor
		p.pushMatrix();
		DrawUtil.setDrawCenter(p);
		p.translate(0, p.height * 0.3f);
		PShapeUtil.setColorForJoons(JoonsWrapper.MATERIAL_MIRROR, p.color(100, 0, 255));
		p.box(p.height * 4, 2, p.height * 4);
		p.popMatrix();

	}
}