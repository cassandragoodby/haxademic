package com.haxademic.core.draw.filters.pshader;

import com.haxademic.core.draw.filters.pshader.shared.BaseFragmentShader;

import processing.core.PApplet;

public class FeedbackRadialFilter
extends BaseFragmentShader {

	public static FeedbackRadialFilter instance;
	
	public FeedbackRadialFilter(PApplet p) {
		super(p, "haxademic/shaders/filters/feedback-radial.glsl");
		setAmp(1f);
		setSampleMult(1f);
		setWaveAmp(1f);
		setWaveFreq(1f);
		setAlphaMult(1f);
	}
	
	public static FeedbackRadialFilter instance(PApplet p) {
		if(instance != null) return instance;
		instance = new FeedbackRadialFilter(p);
		return instance;
	}
	
	public void setAmp(float amp) {
		shader.set("amp", amp);
	}
	
	public void setSampleMult(float samplemult) {
		shader.set("samplemult", samplemult);
	}

	public void setWaveAmp(float waveAmp) {
		shader.set("waveAmp", waveAmp);
	}

	public void setWaveFreq(float waveFreq) {
		shader.set("waveFreq", waveFreq);
	}
	
	public void setAlphaMult(float alphaMult) {
		shader.set("alphaMult", alphaMult);
	}
	
}
