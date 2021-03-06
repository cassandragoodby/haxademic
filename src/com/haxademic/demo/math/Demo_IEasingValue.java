package com.haxademic.demo.math;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.ElasticFloat;
import com.haxademic.core.math.easing.IEasingValue;
import com.haxademic.core.math.easing.LinearFloat;

public class Demo_IEasingValue
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	protected IEasingValue[] easings;
	
	public void setupFirstFrame() {
		easings = new IEasingValue[] {
			new EasingFloat(0, 0.2f),
			new LinearFloat(0, 5),
			new ElasticFloat(0, .75f, .40f),
		};
	}
	
	public void drawApp() {
		// set up drawing context
		background(0);
		DrawUtil.setDrawCenter(p);
		p.fill(255);
		
		// apply easings & show result
		for (int i = 0; i < easings.length; i++) {
			easings[i].setTarget(p.mouseX);
			easings[i].update();
			p.rect(easings[i].value(), (p.height/4) * (i+1), 40, 40);
		}
		
		// reset current value every 200 frame
		if(p.frameCount % 200 == 0) {
			for (int i = 0; i < easings.length; i++) {
				easings[i].setCurrent(0);
			}	
		}
	}

}
