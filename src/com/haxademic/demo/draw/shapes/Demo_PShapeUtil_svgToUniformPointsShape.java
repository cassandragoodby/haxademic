package com.haxademic.demo.draw.shapes;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.filters.pshader.BlurProcessingFilter;
import com.haxademic.core.draw.shapes.PShapeUtil;
import com.haxademic.core.file.FileUtil;

import processing.core.PShape;

public class Demo_PShapeUtil_svgToUniformPointsShape 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected float modelHeight;
	protected PShape svg;

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.LOOP_FRAMES, 240);
	}
	
	protected void setupFirstFrame() {
//		svg.setStroke(false);

		svg = PShapeUtil.svgToUniformPointsShape(FileUtil.getFile("haxademic/svg/hexagon.svg"), 20);
		svg.disableStyle();
		PShapeUtil.centerShape(svg);
		PShapeUtil.scaleShapeToExtent(svg, p.height * 0.8f);
	}

	public void drawApp() {		
		if(p.frameCount == 1) background(0);
		for(int i=0; i < 10; i++) DrawUtil.feedback(p.g, 2f);
		BlurProcessingFilter.instance(p).setBlurSize(6);
		BlurProcessingFilter.instance(p).applyTo(p.g);
		
		// rotate
		p.translate(p.width/2f, p.height/2f, -width*1.5f);
		p.rotateX(P.QUARTER_PI);
		p.rotateZ(loop.progressRads()); /// -P.HALF_PI +
		p.scale(1f + 0.2f * P.sin(p.loop.progressRads()));
		// draw mesh with texture or without
		p.stroke(
				255, 
				127 + 127 * P.sin(p.loop.progressRads() * 3f),
				127 + 127 * P.sin(P.PI + p.loop.progressRads()));
		p.strokeWeight(10 + 8f * P.sin(P.PI + p.loop.progressRads()));
		p.shape(svg);
	}
		
}