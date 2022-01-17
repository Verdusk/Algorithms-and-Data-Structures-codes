

import java.util.Scanner;

public class AVLTreeLong 
{
	//AVL Tree for long elements.
	
	//Experimental: in rotations, there were some lines that should be unnecessary, that calculates subsize of node and b.
	//These should be unnecessary as they're calculated anyway at the end of the function via update function.
	//The unnecessary lines are removed in this version. Correctness testing pending.
	
	//Delete is still only loosely tested, not necessarily correctly implemented!
	
	static class AVLTree
	{ //AVL TREE!!!
		Node root = null;
		int nodes = 0;
		
		//I think this was O(log n)
		int getNumberOfEqualOrGreater(long val)
		{
			return getNumberOfEqualOrGreater(val, root);
		}
		
		private int getNumberOfEqualOrGreater(long val, Node pos)
		{
			if(pos==null)return 0;
			if(val == pos.value)
			{
				if(pos.r != null)return pos.r.subsize+1;
				else return 1;
			}
			else if(val < pos.value)
			{
				int fromR = 0;
				if(pos.r != null)fromR = pos.r.subsize;
				return getNumberOfEqualOrGreater(val, pos.l) + fromR + 1;
			}
			else if(val > pos.value)
			{
				return getNumberOfEqualOrGreater(val, pos.r);
			}
			
			return 0;
		}
		
		//I think this was O(log n)
		int getNumberOfEqualOrLesser(long val)
		{
			return getNumberOfEqualOrLesser(val, root);
		}
		
		private int getNumberOfEqualOrLesser(long val, Node pos)
		{
			if(pos==null)return 0;
			if(val == pos.value)
			{
				if(pos.l != null)return pos.l.subsize+1;
				else return 1;
			}
			else if(val > pos.value)
			{
				int fromL = 0;
				if(pos.l != null)fromL = pos.l.subsize;
				return getNumberOfEqualOrLesser(val, pos.r) + fromL + 1;
			}
			else if(val < pos.value)
			{
				return getNumberOfEqualOrLesser(val, pos.l);
			}
			
			return 0;
		}
		
		void add(long i)
		{
			root = insert(root, i);
		}
		
		private Node insert(Node pos, long i)
		{
			if(pos == null)
			{ //place here
				nodes++;
				return new Node(i);
			}
			if(pos.value == i)return pos; //already here!
			
			if(i < pos.value)
			{ //go left
				pos.l = insert(pos.l, i);
			}
			else
			{
				pos.r = insert(pos.r, i);
			}
			
			pos.update();
			return balance(pos);
		}
		
		Node getSmallest(Node pos)
		{
			if(pos.l == null)
				return pos;
			else return getSmallest(pos.l);
		}
		
		Node getLargest(Node pos)
		{
			if(pos.r == null)
				return pos;
			else return getLargest(pos.r);
		}
		
		void remove(int i)
		{
			root = delete(root, i);
		}
		
		private Node delete(Node pos, long i)
		{
			if(pos == null)
			{ //already non-existent
				return null;
			}
			if(pos.value == i)
			{
				//delete this
				if(pos.l != null && pos.r != null)
				{
					//you can do either but let's remove from the subtree with bigger height.
					if(pos.l.height >= pos.r.height)
					{
						Node dum = getLargest(pos.l); //return largest of left subtree
						pos.value = dum.value; //copy its value
						pos.l = delete(pos.l, dum.value); //then remove that, while updating all nodes necessary
					}
					else
					{
						Node dum = getSmallest(pos.r); //return smallest of right subtree
						pos.value = dum.value; //copy its value
						pos.r = delete(pos.r, dum.value); //then remove that, while updating all nodes necessary
					}
				}
				else if(pos.l != null)
				{
					pos = pos.l;
				}
				else if(pos.r != null)pos = pos.r;
				else //both are null
				{
					return null;
				}
			}
			
			else if(i < pos.value)
			{ //go left
				pos.l = delete(pos.l, i);
			}
			else
			{
				pos.r = delete(pos.r, i);
			}
			
			pos.update();
			return balance(pos);
		}
		
		Node leftRotate(Node node)
		{
			Node b = node.r;
			node.r = b.l;
			b.l = node;
			node.update();
			b.update();
			return b;
		}
		
		Node rightRotate(Node node)
		{
			Node b = node.l;
			node.l = b.r;
			b.r = node;
			node.update();
			b.update();
			return b;
		}
		
		Node llCase(Node node)
		{
			return rightRotate(node);
		}
		
		Node lrCase(Node node)
		{
			node.l = leftRotate(node.l);
			return rightRotate(node);
		}
		
		Node rrCase(Node node)
		{
			return leftRotate(node);
		}
		
		Node rlCase(Node node)
		{
			node.r = rightRotate(node.r);
			return leftRotate(node);
		}
		
		Node balance(Node node)
		{
			if(node.balance <= -2)
			{
				if(node.l.balance <= 0)
					return llCase(node);
				else
					return lrCase(node);
			}
			else if(node.balance >= 2)
			{
				if(node.r.balance >= 0)
					return rrCase(node);
				else
					return rlCase(node);
			}
			
			return node; //already balanced!
		}
		
		void printout(Node pos)
		{
			if(pos == null)return;
			pos.printout();
			if(pos.l != null)System.out.println(pos.value + "'s L");
			printout(pos.l);
			if(pos.r != null)System.out.println(pos.value + "'s R");
			printout(pos.r);
		}
		
		static class Node
		{
			int balance = 0;
			int height = 0;
			long value;
			
			int subsize = 1; //size of subtree rooted here, i.e. descendants + 1 (self)
			
			Node l = null;
			Node r = null;
			
			void printout()
			{
				System.out.println("Value " + value + " height " + height + " subsize " + subsize + " balance " + balance);
			}
			
			void update()
			{ //leaf has height of 0.
				subsize = 1;
				if(l != null)subsize += l.subsize;
				if(r != null)subsize += r.subsize;
				
				int lh = -1;
				int rh = -1;
				if(l!=null)lh = l.height;
				if(r!=null)rh = r.height;
				height = 1 + Math.max(lh, rh);
				
				balance = rh - lh;
			}
			
			public Node(long i)
			{
				value = i;
			}
		}
	}
	
	//COPY ENDS HERE
	
	static void traverse(AVLTree.Node node)
	{
		if(node==null)return;
		node.printout();
		System.out.println(node.value + "'s l:");
		traverse(node.l);
		System.out.println(node.value + "'s r:");
		traverse(node.r);
	}

	static int test(int a)
	{
		a = a*2;
		return a;
	}
	
	public static void main(String[] args) 
	{
		System.out.println(test(82));
		AVLTree avl = new AVLTree();
		
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		for(int i = 1; i <= n; i++)
		{
			//find how many entries there are that is greater i
			
			int cur = sc.nextInt();
			avl.add(cur);
		}
		
		System.out.println("Delete how many elements?");
		n = sc.nextInt();
		for(int i = 0; i < n; i++)
		{
			int cur = sc.nextInt();
			avl.remove(cur);
		}
		avl.printout(avl.root);
		int m = sc.nextInt();
		
		System.out.println(avl.getNumberOfEqualOrGreater(m));
	}

}
