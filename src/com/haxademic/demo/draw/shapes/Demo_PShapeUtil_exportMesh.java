package com.haxademic.demo.draw.shapes;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.shapes.Icosahedron;
import com.haxademic.core.draw.shapes.PShapeUtil;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class Demo_PShapeUtil_exportMesh 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected PShape shapeIcos;
	protected PImage img;

	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.LOOP_FRAMES, 240 );
		p.appConfig.setProperty( AppSettings.WIDTH, 800 );
		p.appConfig.setProperty( AppSettings.HEIGHT, 800 );
	}

	public void setup() {
		super.setup();	

		p.sphereDetail(10);
		float extent = p.width/5f;
		shapeIcos = Icosahedron.createIcosahedron(p.g, 4, img);
		PShapeUtil.scaleShapeToExtent(shapeIcos, extent);
		
		
		PShape newIcos = p.createShape();
		newIcos.beginShape(PConstants.TRIANGLES);
		PVector v = new PVector();
		for (int i = 0; i < shapeIcos.getVertexCount(); i+=3) {
			// get current vertex
			shapeIcos.getVertex(i, v);
			v.mult(4f); // scale up for noise since scaleSvgToExtent doesn't change the actual vertices
			float noiseExclude = p.noise(v.x, v.y, v.z);
			// selectively add faces depending on noise
			if(noiseExclude > 0.45f) {
				shapeIcos.getVertex(i, v);
				v.mult(extent * (1f + p.noise(v.x, v.y, v.z)));
				newIcos.vertex(v.x, v.y, v.z);
				shapeIcos.getVertex(i+1, v);
				v.mult(extent * (1f + p.noise(v.x, v.y, v.z)));
				newIcos.vertex(v.x, v.y, v.z);
				shapeIcos.getVertex(i+2, v);
				v.mult(extent * (1f + p.noise(v.x, v.y, v.z)));
				newIcos.vertex(v.x, v.y, v.z);
			}
		}
		newIcos.endShape();
		shapeIcos = newIcos;
	}

	public void drawApp() {
		background(255);
		
		// setup lights
		DrawUtil.setBetterLights(p);
		
		// icosahedron
		p.pushMatrix();
		p.translate(p.width/2f, p.height/2f);
		p.rotateY(loop.progressRads());
		shapeIcos.disableStyle();
		p.fill(20);
		p.noStroke();
//		p.stroke(0);
		p.shape(shapeIcos);
		p.popMatrix();
		
		if(p.frameCount == 100) {
			PShapeUtil.exportMesh(shapeIcos);
		}
	}
	
}