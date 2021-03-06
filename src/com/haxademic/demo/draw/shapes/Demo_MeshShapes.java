package com.haxademic.demo.draw.shapes;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.shapes.MeshShapes;

public class Demo_MeshShapes
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected MeshShapes meshes[];
	protected int meshIndex = 0;

	protected void overridePropsFile() {
		appConfig.setProperty( AppSettings.WIDTH, "1000" );
		appConfig.setProperty( AppSettings.HEIGHT, "800" );
	}

	public void setup() {
		super.setup();
		// init all shapes
		meshes = new MeshShapes[] {
				new MeshShapes(MeshShapes.PLANE, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.TUBE, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.SPHERE, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.TORUS, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.PARABOLOID, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.STEINBACHSCREW, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.SINE, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.FIGURE8TORUS, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.ELLIPTICTORUS, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.BOHEMIANDOME, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.BOW, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.MAEDERSOWL, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.ASTROIDALELLIPSOID, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.TRIAXIALTRITORUS, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.LIMPETTORUS, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.HORN, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.SHELL, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.KIDNEY, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.LEMNISCAPE, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.TRIANGULOID, 200, 200, -4, 4, -PI, PI),
				new MeshShapes(MeshShapes.SUPERFORMULA, 200, 200, -4, 4, -PI, PI),
		};
		// set colors
		for (int i = 0; i < meshes.length; i++) {
			meshes[i].setColorRange(192, 192, 50, 50, 50, 50, 100);
		}
	}

	public void drawApp() {
		p.background(0);
		noStroke();
		fill(0);
		DrawUtil.setBetterLights(p);
		DrawUtil.setCenterScreen(p);
		rotateX(0.5f); 
		rotateY(p.mousePercentX() * 10f); 
		meshIndex = P.floor(p.mousePercentY() * meshes.length); 
		// draw shape
		scale(80);
		meshes[meshIndex].draw(p.g);
	}

}