package DeApp1.plot;

import java.awt.*;         // Import all classes from the java.awt package
                           // AWT is the Abstract Window Toolkit. The AWT
import java.io.*;
import DeApp1.screen.*;           // Import screens
import DeApp1.de.*;


public class PlotGraph1 extends Canvas
/***********************************************************
**                                                        **
**                                                        **
** Authors:            Mikal Keenan                       **
**                     Rainer Storn                       **
**                                                        **
** Date:               3/16/98                            **
**                                                        **
** Use PlotGraph2 to adjust the graphics.                 **
***********************************************************/
{
// The axes, and tolerance scheme are computed once and plotted into a
// static image (staticI). The sample data is plotted into a background
// image and copied into the Animation's gc to avoid flicker.

	protected Image    staticImage;
	protected Graphics staticGraphics    = null;
	protected Image    offscreenImage ;
	protected Graphics offscreenGraphics = null;

	protected boolean ready = false;

	int choice = 1;

	protected DEScreen deScreen;

    double best[];
    int dim;

	int plotting_samples  = 60;  // (or 60) make this problem dependent!

	protected Dimension Area;
	
	protected int x;                                   
	protected int y;
	protected int w;
	protected int h;

	protected double min_x;        // Relative coordinates
	protected double max_x;
	protected double min_y;
	protected double max_y;

	protected int    abs_min_x;    // Absolute coordinates
	protected int    abs_max_x;
	protected int    abs_min_y;
	protected int    abs_max_y;

	protected int    x_tics;  // Number of tics in X-direction
	protected int    y_tics;  // Number of tics in Y-direction

/*
  Description of the graphics screen

             abs_min_x                    abs_max_x
            ------------------------------------w
           |
abs_min_y  |     --------------------------
           |    |
           |    |
           |    |     ((abs_max_x-abs_min_x)/2, (abs_max_y-abs_min_y)/2)
           |    |      is center of drawing area
           |    |
abs_max_y  |    |
           |
           h
*/


    Dimension minSize;
    int margin = 40;



  public PlotGraph1(DEScreen father, int width, int height)
  /***********************************************************
  ** Set size of the plot and define the axes.              **
  ***********************************************************/
  {
	deScreen = father;
    minSize  = new Dimension(width-margin,height-margin);  //set minimum size
  }

  public Dimension preferredSize()
  /***********************************************************
  ** The layout manager needs this to determine the right   **
  ** size.                                                  **
  ***********************************************************/
  {
     return minimumSize();
  }

  public synchronized Dimension minimumSize()
  /***********************************************************
  ** The layout manager needs this to determine the right   **
  ** size.                                                  **
  ***********************************************************/
  {
     return minSize;
  }

  int absX (double x)
  /***********************************************************
  ** Transform relative X-values in absolute ones.          **
  ***********************************************************/
  {
	return abs_max_x + (int) (((double)(abs_min_x-abs_max_x)) * 
		   ((max_x-x)/(max_x-min_x)));
  }
	
  int absY (double y)
  /***********************************************************
  ** Transform relative Y-values in absolute ones.          **
  ***********************************************************/
  {
	return abs_min_y + (int) (((double)(abs_max_y-abs_min_y)) * 
		   ((max_y-y)/(max_y-min_y)));
  }

  void initParameters()
  /*******************************************************
  ** Set some parameters.                               **
  *******************************************************/
  {
		x            = 0;
		y            = 0;
		w            = size().width;
		h            = size().height;

		abs_min_x      = w / 8;           // Compute some variables
		abs_max_x      = w * 7 / 8;
		abs_min_y      = h / 8;
		abs_max_y      = h * 7 / 8;

		min_x         =  0;            // Mimimum abscissa value
		max_x         =  9;
		min_y         =  -10;          // Minimum ordinate value
		max_y         =  +10;
	    x_tics        =   9;  // Number of tics in X-direction
	    y_tics        =   8;  // Number of tics in Y-direction
  }


  public synchronized void initGraphics()
  /***********************************************************
  ** Initializes background graphics, computes the          **
  ** tolerance scheme, etc.                                 **
  ***********************************************************/
  {	
	  /*---static part of the graphics-------------*/
		staticImage    = createImage (w,h);	 // create a static image
		staticGraphics = staticImage.getGraphics();	// graphics context for the 
		                                            // static image.
		staticGraphics.setColor (Color.white); // white background
		staticGraphics.fillRect (x, y, w, h);  // in rectangle area.
		preparePlot (staticGraphics); // plot axes and tolerance scheme

	  /*---dynamic part of the graphics------------*/
		offscreenImage    = createImage (w, h);
		offscreenGraphics = offscreenImage.getGraphics();

	  /*---draw the static image on the graphics context---*/
	  /*---offscreenGraphics (location x=0, y=0, no--------*/
	  /*---ImageObserver).---------------------------------*/
		offscreenGraphics.drawImage (staticImage, 0, 0, null);
   }

  void init()
  /*******************************************************
  ** As the name says: Initialization.                  **
  *******************************************************/
  {
	ready   = false;
	initParameters();
	initGraphics();
	ready   = true;
  }

  void preparePlot (Graphics staticGraphics)
  /*******************************************************
  ** Draws the static part of the plot.                 **
  *******************************************************/
  {
	plotAxes (staticGraphics);
  }
	
  public void plotAxes (Graphics g)
  /*******************************************************
  ** Plot coordinate system in which polynomial will be **
  ** plotted.                                           **
  *******************************************************/
  {
	int tick_height = 3;
	int i; // counter variable
    //System.out.println("Plot Axes");

	g.setColor (Color.black); 

	/*---Draw x-axis----------------------------*/
    int static0 = absY (0.0);
	g.drawLine (absX (min_x), static0, absX (max_x), static0);      // X axis

	/*---Draw y-axis----------------------------*/
 	static0 = absX (0.0);
 	g.drawLine (static0, absY (min_y), static0, absY (max_y));      // Y axis

	/*---Prepare x-axis ticks-------------------*/
    static0 = absY (0.0);
    int base_pos = absX (min_x);
    int static1  = static0;
    int static2  = static0 - tick_height;

	double increment = ((double) (abs_max_x - abs_min_x) / 
		                (double) x_tics) + 0.25;	   
	/*---Draw x-axis ticks----------------------*/
	for (i = 0;  i <= x_tics;  i++)        
	{ 
	  int x = base_pos + (int) (i * increment);
	  g.drawLine (x, static1, x, static2);
    }	

	/*---Prepare y-axis ticks-------------------*/
    base_pos = absY (min_y);
    static0  = absX (0.0);
    static1  = static0 - 2;
    static2  = static0 + 2;

	increment = ((double) (abs_max_y - abs_min_y) / 
		         (double) y_tics); 

	/*---Draw y-axis ticks----------------------*/
	for (i = 0;  i <= y_tics;  i++)                       
	{ 
	  int y = base_pos - (int) (i * increment);
	  g.drawLine (static1, y, static2, y);
    } 

	/*---Prepare x-tick labeling----------------*/
	g.setFont (new Font ("Helvetica", Font.PLAIN, 10));      

	static0 = absY (0) - 10;
	double x = min_x;

    increment = (double) (max_x - min_x) / (double) x_tics;

	/*---Draw x-tick labeling-------------------*/
	for (i = 0;  i <= x_tics;  i++, x += increment)
    {
	  Double DblObj = new Double (x);
	  g.drawString (DblObj.toString(), absX (x) - 10, static0);
	}

	Double DblObj = new Double (max_y);
	g.drawString (DblObj.toString(), absX (0), absY (max_y) - 8);
  }





  public void plot (Graphics g)
  /*******************************************************
  ** Plots the current polynomial.                      **
  *******************************************************/
  {
	g.setColor (Color.blue);
	best = deScreen.getBest();
	dim  = deScreen.getDimension();
	double x, y, sign;
	int    i;
    for (i = 0;  i <dim;  i++)
    {
	  x = (double)i;
	  if (best[i] < 0) sign = -1;
	  else sign = +1;
	  y = best[i];
	  g.drawLine (absX (x), absY (0), absX (x), absY (y));
	  g.drawLine (absX (x), absY (y), absX (x+1), absY (y));
	  g.drawLine (absX (x+1), absY (y), absX (x+1), absY (0));
    } 
  }


  public void paint (Graphics g)
  /*******************************************************
  ** Actually draws on the canvas.                      **
  *******************************************************/
  {
	init();	// initializing with every paint() call 
	        // allows for resizing of the plot screen
	g.drawImage (offscreenImage, 0, 0, null);
  }

  public void refreshImage()
  /***********************************************************
  ** Update function which recomputes the variable screen   **
  ** image.                                                 **
  ***********************************************************/
  {
	if (offscreenGraphics == null)
	{
		init();
	}

    offscreenGraphics.drawImage (staticImage, 0, 0, null); 
	plot (offscreenGraphics);
	repaint();	  
  }

  public void update(Graphics g)
  /*******************************************************
  ** Overriding update() reduces flicker. The normal    **
  ** update() method clears the screen before it        **
  ** repaints and hence causes flicker. We dont like	**
  ** this and leave out the screen clearing.			**
  *******************************************************/
  {
	g.drawImage (offscreenImage, 0, 0, null);
  }




}




