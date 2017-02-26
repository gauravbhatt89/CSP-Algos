package CSP;

import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import java.awt.geom.Line2D;
import java.math.*;

public class cspAlgo
{
	static List<point> neighborList=new ArrayList<>();
	static List<point> pointsList= new ArrayList<>();
	static List<Line> lineList= new ArrayList<>();
	static int MAXPOINTS =  30;
	static int maxcolor = 4;
	
	
	public static void main(String args[])
	{
		
		List <Line> lines=new ArrayList<>();
        Random rn = new Random();
		for(int i=0;i<MAXPOINTS;i++){	
			point p = new point();
			
			p.x = rn.nextInt(MAXPOINTS );
			p.y = rn.nextInt(MAXPOINTS );
			p.index = i;
			pointsList.add(p);
			System.out.println("Point " + p.index + " with coordinates " + " X: " + p.x + ", Y: " + p.y);
		}
		for (int j = 0; j< MAXPOINTS; j++) {
			for (int i=0; i<MAXPOINTS; i++) {
				Double d = Math.sqrt(((pointsList.get(i).x - pointsList.get(j).x) * 
									  (pointsList.get(i).x - pointsList.get(j).x)) +
								  	  (pointsList.get(i).y - pointsList.get(j).y) * 
								      (pointsList.get(i).y - pointsList.get(j).y));
				dist dis = new dist();
				dis.index = i;
				dis.dist = d;
				pointsList.get(j).distance.add(dis);
				
				System.out.println("Distance of point " + j + " From point " + i + " is: " + d);
				System.out.println(pointsList.get(j).distance.get(i).index);
			}
		}

		compare c = new compare();

		ArrayList<Integer> list = new ArrayList();
        for (int i=0; i<MAXPOINTS; i++) {
            list.add(new Integer(i));
        }    
        Collections.shuffle(list);
        
		for (int k = 0; k < MAXPOINTS; k++) {
			int n1 = list.get(k);
			int min=1;		
			pointsList.get(n1).distance.sort(c);
		}			
		for (int j=0;j<MAXPOINTS;j++){
			System.out.println("\n\n Sorted nodes w.r.t. distance from node " + j + " are: ");
			for(int i=0;i<MAXPOINTS;i++){
				System.out.print(" " + pointsList.get(j).distance.get(i).index);
			}
		}
		
		System.out.println("\n\n *** CREATING LINES *** ");
		Random rm = new Random(MAXPOINTS);
		int maxlines = MAXPOINTS*MAXPOINTS;
		while(maxlines >0) {
			int i = rm.nextInt(MAXPOINTS);
			for (int j=1;j<MAXPOINTS;j++) {
		   	 /* create a line if there not already a line b/w 2 points 
			    and its not intersecting with other line. */
				int secondpoint=pointsList.get(i).distance.get(j).index;
				if ((lines.contains(new Line(i,secondpoint))) || 
					(lines.contains(new Line(secondpoint, i)))) {
						// Do Nothing.
					} else {
						boolean result = false;
						for(int k=0;k<lines.size();k++){
							if (i==lines.get(k).p1 || i==lines.get(k).p2 ||
									secondpoint == lines.get(k).p1 || secondpoint == lines.get(k).p2){
								// Do Nothing.
							} else {
								Line2D line1 = new Line2D.Float(pointsList.get(i).x, pointsList.get(i).y, pointsList.get(secondpoint).x, pointsList.get(secondpoint).y);
								Line2D line2 = new Line2D.Float(pointsList.get(lines.get(k).p1).x, pointsList.get(lines.get(k).p1).y, pointsList.get(lines.get(k).p2).x, pointsList.get(lines.get(k).p2).y);
								result = line2.intersectsLine(line1);
								if (result){
									break;
								}
							}
						}
						if (!result) {
							Line ll= new Line(i, secondpoint); 
							if(!((lines.contains(new Line(i,secondpoint))) || 
									(lines.contains(new Line(secondpoint, i)))))
								lines.add(ll);
						}
					}
				}
			
			maxlines--;
		}
		
		for (int ii=0;ii<lines.size();ii++)
			System.out.println("Lines are : " +lines.get(ii).p1 + " " +lines.get(ii).p2);
		
		
		/* Maintain list of all neighbors of a node */
		for (int jj=0;jj<MAXPOINTS;jj++) {
			for (int ii=0;ii<lines.size();ii++) {
				if((pointsList.get(jj).index == lines.get(ii).p1)) {
					pointsList.get(jj).neighbor.add(lines.get(ii).p2);
				}
				if ((pointsList.get(jj).index == lines.get(ii).p2)){
					pointsList.get(jj).neighbor.add(lines.get(ii).p1);		
				}
			}
		}

		for (int jj=0;jj<MAXPOINTS;jj++) {
			System.out.print("\nNode "+pointsList.get(jj).index +" has neighbor: ");
			for (int kk=0;kk<pointsList.get(jj).neighbor.size();kk++) {
				System.out.print(pointsList.get(jj).neighbor.get(kk));
			    System.out.print(" ");
			}
		}
		System.out.println();
	
	
		Scanner s = new Scanner(System.in);
		System.out.println("\nAn instance of the Problem is created.");
		System.out.println("Press 1 to solve with Min-Conflict CSP Algo.\n"
						 + "Press 2 to solve with BackTracking CSP Algo.\n"
						 + "Press 3 to solve with BackTracking with Fwd Checking CSP Algo.\n"
						 + "Press 4 to solve with Min-Conflict MAC CSP Algo.\n");
		int aa = s.nextInt();
		if (aa == 1) {
			long startTime = System.nanoTime();
			minconflictCSP();
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println("Time Taken by minconflictCSP is " +duration/1000 +"usec");
		} else if (aa==2) {
			long startTime = System.nanoTime();
			backtrackingCSP();
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println("Time Taken by backtrackingCSP() is " +duration/1000 +"usec");
		} else if (aa==3) {
			long startTime = System.nanoTime();
			btfwdcheckingCSP();
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println("Time Taken by btfwdchecking1() is " +duration/1000 +"usec");	
		} else if (aa==4) {
			long startTime = System.nanoTime();
			macCSP();
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println("Time Taken by macCSP1() is " +duration/1000 +"usec");	
		} else { 
			System.out.println("Please Enter valid Input!\n");
			s.close();
			return;
		}
		s.close();
	}
	

	
	public static boolean macCSP()
	{
		System.out.println("\n*** MAC CSP ***");
		int startnode = 0;
		int startcolor = 0;
		
		point[] colors = new point[MAXPOINTS];
		boolean result;
		List<Integer>[] domain = new ArrayList[MAXPOINTS]; 
		for(int i=0;i<MAXPOINTS;i++){
			domain[i]= new ArrayList<Integer>();
			for(int j=0;j<maxcolor;j++){
				domain[i].add(new Integer(j));
			}
		}
		for (int i=0;i<MAXPOINTS;i++){
			colors[i] = new point();
		}
		result = fwdcheck1(startnode, startcolor, colors,domain);
		
		return result;
	}

	public static boolean fwdcheck1(int startnode, int startcolor, point[] colors,List<Integer>[] domain) 
	{	
		System.out.println("startnode = "+startnode+" startcolor = "+startcolor);
		point p= new point();
		p.index=startnode;
		p.color=startcolor;	
	//	point[] colors = new point[MAXPOINTS];
		
	//	for (int jj=0;jj<startnode;jj++){
	//	colors[jj] = c[jj];
	//	}
		colors[startnode]=p;
		
	/*	List<Integer>[] domain= new ArrayList[MAXPOINTS];
		for(int i=0;i<MAXPOINTS;i++){
			domain[i]= new ArrayList();
			for(int j=0;j<domain[i].size();j++){
				domain[i].add(new Integer(d[i].get(j)));
			}
		}
		*/
		domain[startnode].clear();
		domain[startnode].add(new Integer(startcolor));
	
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(startnode);
		
		
		while(!q.isEmpty()) {
			for (int ii=0;ii<pointsList.get(startnode).neighbor.size();ii++) {
				if(startnode<pointsList.get(startnode).neighbor.get(ii))
		    	   domain[pointsList.get(startnode).neighbor.get(ii)].remove(new Integer(startcolor));
				if(domain[pointsList.get(startnode).neighbor.get(ii)].size()==1) {
				   q.add(pointsList.get(startnode).neighbor.get(ii));
				}
			}
			startnode = (int) q.poll();
		}
		System.out.println("startnode' = "+startnode+" startcolor' = "+startcolor);
		
		if(startnode>=MAXPOINTS-1){
			if(!bcconflict(startnode, colors)){
					for(int ii=0; ii<colors.length; ii++){
						System.out.println("The color Assigned to "+ii+" is "+colors[ii].color);
					}	
				return true;
			}
			return false;
		}
		
		if(!bcconflict(startnode, colors)){	
			for(int i=0; i<maxcolor; i++){
				System.out.println("domain of "+(startnode+1)+" is"+domain[startnode+1]);
				if(domain[startnode+1].contains(new Integer(i))&&fwdcheck1(startnode+1, i, colors,domain)){
					return true;
				}	
				System.out.println("BC'");
			}
			return false;
		} else {
			return false;
		}
		//TO_DO: Need to check this:/*domain[startnode+1].contains(new Integer(i))&&*/
	}
	
	public static boolean btfwdcheckingCSP()
	{
		System.out.println("\nBackTracking with Forward Checking ");
		int startnode =0;
		int startcolor = 0;
		point[] colors = new point[MAXPOINTS];
		boolean result;
		List<Integer>[] domain = new List[MAXPOINTS]; 
		for(int i=0;i<MAXPOINTS;i++){
			domain[i]= new ArrayList<Integer>();
			for(int j=0;j<maxcolor;j++){
				domain[i].add(new Integer(j));
			}
		}
		for (int i=0;i<MAXPOINTS;i++){
			colors[i] = new point();
		}
		result = fwdcheck(startnode, startcolor, colors,domain);
		if (!result)
			System.out.println("BC");
	
		return result;
	}
	
	public static boolean fwdcheck(int startnode, int startcolor, point[] c,List<Integer>[] domain) 
	{	
		System.out.println("startnode = "+startnode+" startcolor = "+startcolor);
		point p= new point();
		p.index=startnode;
		p.color=startcolor;	
		point[] colors = new point[MAXPOINTS];
		
		for (int jj=0;jj<=startnode;jj++){
			colors[jj]=c[jj];
	//		System.out.println("Node "+colors[jj].index+" color = "+colors[jj].color);
		}
		
		colors[startnode]=p;
		//colors[startnode].color=p.color;
		
		//List<Integer> [] domain= new ArrayList[MAXPOINTS];
	/*	for(int i=0;i<MAXPOINTS;i++){
			domain[i]= new ArrayList<Integer>();
			for(int j=0;j<maxcolor;j++){
				domain[i].add(new Integer(d[i].get(j)));
			}
		}
		*/
		domain[startnode].clear();
		domain[startnode].add(new Integer(startcolor));
		
		for (int ii=0;ii<pointsList.get(startnode).neighbor.size();ii++) {
			if(startnode<pointsList.get(startnode).neighbor.get(ii))
			   domain[pointsList.get(startnode).neighbor.get(ii)].remove(new Integer(startcolor));	
			if(domain[pointsList.get(startnode).neighbor.get(ii)].size()==0)
			   continue;
		}
		
		if(startnode>=MAXPOINTS-1){
			if(!bcconflict(startnode, colors)){
				for(int ii=0; ii<colors.length; ii++){
					System.out.println("The color Assigned to "+ii+" is "+colors[ii].color);
				}
				return true;
			}
			return false;
		}
				System.out.println("what the");
		if(!bcconflict(startnode, colors)){	
			for(int i=0; i<maxcolor; i++){
				System.out.println("domain of "+(startnode+1)+" is"+domain[startnode+1]);
				for(int j =0; j<domain[startnode+1].size();j++){
					System.out.println("looking for "+i+" in domain of node "+(startnode+1)+" is: "+domain[startnode +1].get(j));
				}
				if(domain[startnode+1].contains(new Integer(i))) {
					if(fwdcheck(startnode+1, i, colors,domain)==true){
						return true;
					}
				}
			}
			return false;
		} else {
			return false;
		}
	    //To_DO: Need to check this : /*domain[startnode+1].contains(new Integer(i))&&*/
	}
	
	
	
	public static boolean bcconflict(int startnode, point[] colors) {
		for (int i= 0; i< pointsList.get(startnode).neighbor.size(); i++){
			if(pointsList.get(startnode).neighbor.get(i)<startnode&&colors[startnode].color == colors[pointsList.get(startnode).neighbor.get(i)].color) {
				//System.out.println("conflict hai mere bhai");
				return true;
			}
		}	
		return false;
	}
	
	
	/* Back Tracking CSP Algorithm */
	public static boolean backtrackingCSP() {
		System.out.println("\nBack Tracking CSP ");
		
		int startnode = 0;
		int startcolor = 0;
		Stack<point> st= new Stack<>();
		
		point pt= new point();
		pt.index=startnode;
		pt.color=startcolor-1;
		st.push(pt);
		
		boolean result = true;
		boolean result1=false;
		while(result || st.size()!=MAXPOINTS){	
			point p2 = st.pop();
			p2.color = p2.color+1;
			pointsList.get(p2.index).color = p2.color;
			st.push(p2);
			if(st.peek().color >= maxcolor){
				st.pop();
				if(st.isEmpty()){
					return false;
				}
				continue;
			}
			result = isconflict(st.peek().index);
				
			if(!result){
				if(st.size() < MAXPOINTS){
					point p = new point();
					p.index = st.peek().index+1;
					p.color = startcolor-1;
					pointsList.get(p.index).color = p.color;
					st.push(p);
					if(st.size() == MAXPOINTS){
						st.peek().color++;
						pointsList.get(st.peek().index).color++;
						result = isconflict(st.peek().index);
						if(result){
							st.peek().color--;
							pointsList.get(st.peek().index).color--;
						}
					}
				}
			}	
		}
		while(!st.isEmpty()){
			System.out.println("The color assigned to node "+st.peek().index+" is "+st.pop().color);
		}
		return true;
	}
	
	/* Check if the current node is having conflict wrt the current color */
	public static boolean isconflict(int startnode) {
		for (int n=0; n < pointsList.get(startnode).neighbor.size();n++) {
			if ((pointsList.get(startnode).color == 
				 pointsList.get(pointsList.get(startnode).neighbor.get(n)).color)&&
				 pointsList.get(startnode).neighbor.get(n)<startnode)
			{
				return true;
			}
		}
		return false;
	}

	
	public static void minconflictCSP() {
		System.out.println("\n*** min-conflict CSP *** ");
		//MAXPOINTS=20;
		
		Random rn = new Random();
		
		boolean result=false;
		
		int y=0;
		
		/* Assign random color to nodes */
		for (int ii =0;ii<MAXPOINTS; ii++) {
			pointsList.get(ii).color = rn.nextInt(maxcolor);
		}
		
		while(!result) {
			int startnode = rn.nextInt(MAXPOINTS);		
			int[][] numconflict = new int[MAXPOINTS][maxcolor];
			
			for (int x=0;x<maxcolor;x++)
				numconflict[startnode][x] = MAXPOINTS;
			for (int m=0;m<maxcolor;m++) {
				int conflict = 0;
				pointsList.get(startnode).color = m;
				for (int ii=0;ii<pointsList.get(startnode).neighbor.size();ii++){
					if(pointsList.get(startnode).color == pointsList.get(pointsList.get(startnode).neighbor.get(ii)).color) {
						conflict++;
					}
				}
				numconflict[startnode][m] = conflict;					
			}
			int MAX = MAXPOINTS;
			for (int i=0;i<maxcolor;i++){
				if(numconflict[startnode][i] <= MAX ) {
					y = i;
					MAX=numconflict[startnode][i];
				}
			}
			pointsList.get(startnode).color = y;
			System.out.println("Assign color " +y +" to Node: "+pointsList.get(startnode).index);
		
			result = isfeasible();
			if(result)
				break;
		}
	}
	
    /* To check if the current assignment is feasible */
	public static boolean isfeasible()
	{
		for (int i=0;i< MAXPOINTS;i++) {
			for (int n=0; n < pointsList.get(i).neighbor.size();n++) {
				if ((pointsList.get(i).color) == 
							(pointsList.get(pointsList.get(i).neighbor.get(n)).color))
					return false;
			}
		}		
		System.out.println("Feasible solution found!");
		for (int i=0;i< MAXPOINTS;i++) {
			for (int n=0; n < pointsList.get(i).neighbor.size();n++) {
					System.out.println("Node " +i +"'s color is "+ pointsList.get(i).color +
							", Neighbor " +pointsList.get(i).neighbor.get(n) +"'s color is " +
							(pointsList.get(pointsList.get(i).neighbor.get(n)).color));
			}
		}
		return true;
	}
	
}

class compare implements Comparator<dist>
{

	@Override
	public int compare(dist x, dist y) {
		if(x.dist < y.dist)
			return -1;
		else if (x.dist > y.dist) 
			return +1;
		else
			return 0;	
	} 
}

class point
{
	int index;
	int color;
	int x,y;
	List<Integer> neighbor = new ArrayList<>();
	List<dist> distance = new ArrayList<>();
}

class dist
{
	int index;
	Double dist;
}

class Line 
{
	int p1, p2;
	
	@Override
	public boolean equals(Object o){
		Line l = (Line)o;
		if ((l.p1 == this.p2) &&  (l.p2 == this.p1))
			return true;
		else 
			return false;
		
	}
	public Line(int a,int b){
		p1=a;
		p2=b;
	}
}

