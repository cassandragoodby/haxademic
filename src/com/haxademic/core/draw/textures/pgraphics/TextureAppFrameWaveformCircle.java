package com.haxademic.core.draw.textures.pgraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.textures.pgraphics.shared.BaseTexture;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class TextureAppFrameWaveformCircle 
extends BaseTexture {

//	protected WaveformData _waveformData;
	protected float _circleInc;
	protected float _amp;
	protected float _strokeWeight;
	protected EasingFloat _radius = new EasingFloat(0, 6);

	public TextureAppFrameWaveformCircle( int width, int height ) {
		super();

		buildGraphics( width, height );
		
//		_waveformData = P.p.audioData.waveform();
		_circleInc = ( (float)Math.PI * 2.0f ) / P.p.audioData.waveform().length;
		
		// set some defaults
		newLineMode();
		_amp = _texture.width / 20f;
		_strokeWeight = 3;
	}
	
	public void newLineMode() {
		_radius.setTarget(MathUtil.randRangeDecimal(_texture.width / 3f, _texture.width / 2f)); 
	}

	public void updateDraw() {
		_texture.clear();
		
		_radius.update();
		
		DrawUtil.resetGlobalProps( _texture );
		DrawUtil.setCenterScreen( _texture );

		int numPoints = P.p.audioData.waveform().length;
		_texture.fill(0);
		_texture.noStroke();
		_texture.beginShape();
		
//		int iNext = 0;
		float radius;//, radiusNext;
		for (int i = 0; i < numPoints; i++ ) {
//			iNext = (i == numPoints - 1) ? 0 : i+1;	// makes sure we wrap around at the end
			radius =     _radius.value() + P.p.audioData.waveform()[i] * _amp;
//			radiusNext = _radius.value() + _waveformData._waveform[iNext] * _amp;
//			p.line( p.sin( _circleInc * i ) * radius , p.cos( _circleInc * i ) * radius, p.sin( _circleInc * iNext ) * radiusNext, p.cos( _circleInc * iNext ) * radiusNext );
			_texture.vertex( P.sin( _circleInc * i ) * radius , P.cos( _circleInc * i ) * radius );
		}
		// connect 1st and last points
		radius = _radius.value() + P.p.audioData.waveform()[0] * _amp;
		_texture.vertex( P.sin( _circleInc * 0 ) * radius , P.cos( _circleInc * 0 ) * radius );
		
		// draw around outer canvas edge
		_texture.vertex( 0, _texture.height/2 );
		_texture.vertex( _texture.width/2, _texture.height/2 );
		_texture.vertex( _texture.width/2, -_texture.height/2 );
		_texture.vertex( -_texture.width/2, -_texture.height/2 );
		_texture.vertex( -_texture.width/2, _texture.height/2 );
		_texture.vertex( 0, _texture.height );

		
		_texture.endShape();
	}
	
}
