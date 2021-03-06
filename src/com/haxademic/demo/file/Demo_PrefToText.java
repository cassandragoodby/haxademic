package com.haxademic.demo.file;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.file.PrefToText;

public class Demo_PrefToText 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected float gray = 0.5f;
	
	public void setupFirstFrame () {
		gray = PrefToText.getValueF("gray", gray);
		P.println("gray", gray);
	}
	
	public void keyPressed() {
		super.keyPressed();
		gray = p.mousePercentX();
		if(p.key == ' ') PrefToText.setValue("gray", gray);
	}
	
	public void drawApp() {
		// set up context
		p.background( gray * 255 );
	}
	
}
