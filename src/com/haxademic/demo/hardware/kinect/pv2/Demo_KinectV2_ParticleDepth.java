package com.haxademic.demo.hardware.kinect.pv2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.file.FileUtil;

import KinectPV2.KinectPV2;
import processing.opengl.PGL;
import processing.opengl.PShader;

public class Demo_KinectV2_ParticleDepth
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	// from: https://github.com/ThomasLengeling/KinectPV2/tree/master/KinectPV2/examples/PointCloudOGL
	
	protected KinectPV2 kinect;


	int  vertLoc;

	//transformations
	float a = 1.9f;
	int zval = -200;
	float scaleVal = 260;


	//value to scale the depth point when accessing each individual point in the PC.
	float scaleDepthPoint = 100.0f;

	//Distance Threashold
	int maxD = 4000; // 4m
	int minD = 0;  //  0m

	//openGL object and shader
	PGL     pgl;
	PShader sh;

	//VBO buffer location in the GPU
	int vertexVboId;

	
	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, 1000 );
		p.appConfig.setProperty( AppSettings.HEIGHT, 800 );
		p.appConfig.setProperty( AppSettings.SHOW_DEBUG, true );
	}

	public void setupFirstFrame() {
		  kinect = new KinectPV2(this);

		  kinect.enableDepthImg(true);

		  kinect.enablePointCloud(true);

		  kinect.setLowThresholdPC(minD);
		  kinect.setHighThresholdPC(maxD);

		  kinect.init();

		  sh = loadShader(FileUtil.getFile("haxademic/shaders/vertex/kinect-points-frag.glsl"), FileUtil.getFile("haxademic/shaders/vertex/kinect-points-vert.glsl"));

		  PGL pgl = beginPGL();

		  IntBuffer intBuffer = IntBuffer.allocate(1);
		  pgl.genBuffers(1, intBuffer);

		  //memory location of the VBO
		  vertexVboId = intBuffer.get(0);

		  endPGL();
	}

	public void drawApp() {
		p.background(0);
		
		  //draw the depth capture images
		  image(kinect.getDepthImage(), 0, 0, 320, 240);
		  image(kinect.getPointCloudDepthImage(), 320, 0, 320, 240);

		  //translate the scene to the center
		  translate(width / 2, height / 2, zval);
		  scale(scaleVal, -1 * scaleVal, scaleVal);
		  rotate(a, 0.0f, 1.0f, 0.0f);

		  // Threahold of the point Cloud.
		  kinect.setLowThresholdPC(minD);
		  kinect.setHighThresholdPC(maxD);

		  //get the points in 3d space
		  FloatBuffer pointCloudBuffer = kinect.getPointCloudDepthPos();

		  // obtain XYZ the values of the point cloud
		  /*
		  stroke(0, 0, 0);
		  for(int i = 0; i < kinect.WIDTHDepth * kinect.HEIGHTDepth; i+=3){
		      float x = pointCloudBuffer.get(i*3 + 0) * scaleDepthPoint;
		      float y = pointCloudBuffer.get(i*3 + 1) * scaleDepthPoint;
		      float z = pointCloudBuffer.get(i*3 + 2) * scaleDepthPoint;
		      
		   
		      point(x, y, z);
		   }
		   */

		  //begin openGL calls and bind the shader
		  pgl = beginPGL();
		  sh.bind();

		  //obtain the vertex location in the shaders.
		  //useful to know what shader to use when drawing the vertex positions
		  vertLoc = pgl.getAttribLocation(sh.glProgram, "vertex");

		  pgl.enableVertexAttribArray(vertLoc);

		  //data size times 3 for each XYZ coordinate
		  int vertData = KinectPV2.WIDTHDepth * KinectPV2.HEIGHTDepth * 3;

		  //bind vertex positions to the VBO
		  {
		    pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexVboId);
		    // fill VBO with data
		    pgl.bufferData(PGL.ARRAY_BUFFER,   Float.BYTES * vertData, pointCloudBuffer, PGL.DYNAMIC_DRAW);
		    // associate currently bound VBO with shader attribute
		    pgl.vertexAttribPointer(vertLoc, 3, PGL.FLOAT, false,  Float.BYTES * 3, 0 );
		  }
		  
		   // unbind VBOs
		  pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);

		  //draw the point buffer as a set of POINTS
		  pgl.drawArrays(PGL.POINTS, 0, vertData);

		  //disable the vertex positions
		  pgl.disableVertexAttribArray(vertLoc);

		  //finish drawing
		  sh.unbind();
		  endPGL();


		  stroke(255, 0, 0);
		  text(frameRate, 50, height - 50);
	}
	
	public void keyPressed() {
		super.keyPressed();
		  if (key == 'a') {
		    zval +=10;
		    println("Z Value "+zval);
		  }
		  if (key == 's') {
		    zval -= 10;
		    println("Z Value "+zval);
		  }

		  if (key == 'z') {
		    scaleVal += 0.1;
		    println("Scale scene: "+scaleVal);
		  }
		  if (key == 'x') {
		    scaleVal -= 0.1;
		    println("Scale scene: "+scaleVal);
		  }

		  if (key == 'q') {
		    a += 0.1;
		    println("rotate scene: "+ a);
		  }
		  if (key == 'w') {
		    a -= 0.1;
		    println("rotate scene: "+a);
		  }

		  if (key == '1') {
		    minD += 10;
		    println("Change min: "+minD);
		  }

		  if (key == '2') {
		    minD -= 10;
		    println("Change min: "+minD);
		  }

		  if (key == '3') {
		    maxD += 10;
		    println("Change max: "+maxD);
		  }

		  if (key == '4') {
		    maxD -= 10;
		    println("Change max: "+maxD);
		  }
		  
		  if(key == 'c'){
		    scaleDepthPoint += 1;
		    println("Change Scale Depth Point: "+scaleDepthPoint);
		  }
		  
		    if(key == 'v'){
		    scaleDepthPoint -= 1;
		    println("Change Scale Depth Point: "+scaleDepthPoint);
		  }
		  
		}
}
