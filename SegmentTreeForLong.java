import java.util.Scanner;

public class SegmentTreeForLong 
{
	/*
	Supports range query and range add and range assign operations, as well as range sum query..
	Can be significantly optimized by implementing the segment tree as an array, instead of using class objects.
	For this program, we use class objects for comprehensibility purposes.
	
	Un-comment System.out.println lines for debugging/logging.
	Refer to link for helpful tutorial: https://cp-algorithms.com/data_structures/segment_tree.html
	
	- Verdusk, 18/1/2022
	*/
	
	static class SegTree
	{
		int a;
		int b;
		
		long sum = 0; //always maintain sum of this subtree
		long lazyAdd = 0; //value to be propagated to both children
		long lazySet = 0; //value to set to. Also means that all elements in range at this point of time has this as the value.
		boolean hasLazySet = false; //if true, all elements in range has been set to lazySet and has precisely that value
		
		//children
		SegTree l;
		SegTree r;
		
		//adds the value "add" to each element in range [q1, q2].
		void AddToInterval(int q1, int q2, long add)
		{
			propagate();
			
			if(q2>b)q2=b; if(q1<a)q1=a; //clamp
			if(q1>q2)return;
			int inrange = q2-q1+1;
			
			
			sum += inrange * add;
			//System.out.println("On [" + a + ", "+ b + "]. Add " + add + " to interval: [" + q1 + ", " + q2 + "], becomes " + sum + ", inrange:" + inrange );
			
			if(q1 == a && q2 == b)
			{ //node's segment is wholly contained by query
				lazyAdd += add;
			}
			else
			{
				if(l!=null)l.AddToInterval(q1, q2, add);
				if(r!=null)r.AddToInterval(q1, q2, add);
			}
		}
		
		void AssignToInterval(int q1, int q2, long set)
		{
			propagate();
			
			if(q2>b)q2=b; if(q1<a)q1=a; //clamp
			if(q1>q2)return;
			int inrange = q2-q1+1;
			
			//System.out.println("On [" + a + ", "+ b + "]. Add " + add + " to interval: [" + q1 + ", " + q2 + "] inrange:" + inrange );
			
			if(q1 == a && q2 == b)
			{ //node's segment is wholly contained by query
				assignToThisNode(set);
				sum = inrange * set;
			}
			else
			{
				if(l!=null)l.AssignToInterval(q1, q2, set);
				if(r!=null)r.AssignToInterval(q1, q2, set);
				
				//if not wholly contained by query, the sum must be calculated from the children (after they are affected by the assign operation)
				sum = 0;
				if(l!=null)sum+=l.sum;
				if(r!=null)sum+=r.sum;
			}
		}		
		void assignToThisNode(long set)
		{
			hasLazySet = true;
			lazySet = set;
			lazyAdd = 0; //assigning value nullifies any previous add operation
		}
		
		int getRangeSize()
		{
			return b-a+1;
		}
		void propagate()
		{
			//set before add. (in case of add then set, the lazyAdd value will have been set to 0)
			
			if(hasLazySet)
			{
				if(l!=null)
				{
					l.lazySet = lazySet;
					l.hasLazySet = true;
					l.lazyAdd = 0;
					l.sum = lazySet*l.getRangeSize();
				}
				if(r!=null)
				{
					r.lazySet = lazySet;
					r.hasLazySet = true;
					r.lazyAdd = 0;
					r.sum = lazySet*r.getRangeSize();
				}
				hasLazySet = false;
			}
			
			
			if(lazyAdd!=0)
			{
			//System.out.println("Propagate " + lazy + " from " + "["+a+", " + ""+b+"] " + (l!=null?"l":"") + (r!=null?"r":""));
				if(l!=null)
				{
					l.lazyAdd+=lazyAdd;
					l.sum += lazyAdd * l.getRangeSize();
				}
				if(r!=null)
				{
					r.lazyAdd+=lazyAdd;
					r.sum += lazyAdd * r.getRangeSize();
				}
				lazyAdd = 0;
			}
		}
		
		/**Gets sum of values across an interval*/
		long GetInterval(int q1, int q2)
		{
			//System.out.println("Get from [" + a + ", " + b + "] with " + q1 + ", " + q2);
			counter++;
			propagate(); //before potentially traversing children, propagate to them any held value.
			//System.out.println("GET " + q1 + " " + q2 + " in " + a + " " + b);
			long ans = 0;
			if(q2>b)q2=b; if(q1<a)q1=a; //clamp
			if(q1>q2)return 0;
			
			//exactly current?
			if(q1 == a && q2 == b)return sum;
			
			//in current...
			int inrange = q2-q1+1;
			//ans+=inrange*sum;
			if(l!=null)ans += l.GetInterval(q1, q2);
			if(r!=null)ans += r.GetInterval(q1, q2);
			return ans;
		}
		
		public SegTree(int a0, int b0)
		{
			a = a0;
			b = b0;
			
			if(a<b)
			{
				int mid = (a+b)/2;
				l = new SegTree(a,mid);
				r = new SegTree(mid+1,b);
			}
		}
		
	}
	
	
	static int counter = 0;

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		SegTree seg = new SegTree(0,n-1);
		
		while(true)
		{
			System.out.println("Input command.");
			String s = sc.next();
			
			if(s.equals("quit"))return;
			else if(s.equals("add"))
			{
				int a = sc.nextInt();
				int b = sc.nextInt();
				long v = sc.nextLong();
				seg.AddToInterval(a, b, v);
			}
			else if(s.equals("set"))
			{
				int a = sc.nextInt();
				int b = sc.nextInt();
				long v = sc.nextLong();
				seg.AssignToInterval(a, b, v);
			}
			else if(s.equals("get"))
			{
				int a = sc.nextInt();
				int b = sc.nextInt();
				counter=0;
				System.out.println(seg.GetInterval(a, b) + ", " + counter + " iterations.");
			}
			
			
		}
	}

}
