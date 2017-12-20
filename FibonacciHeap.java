import java.util.ArrayList;

/**
	 * Java Fibonacci heap
	 *
	 */

	public class FibonacciHeap {

	    private int keyNum;       
	    private Node max;       


	    /**
	     * initial Fibheap
	     * 
	     */
	    public FibonacciHeap() {
	        this.keyNum = 0;
	        this.max = null;
	    }

	    /**
	     * remove node
	     */
	    private void removeNode(Node node) {
	        node.left.right = node.right;
	        node.right.left = node.left;
	    }
	     
	    /**
	     * insert node and update keyNum
	     */ 
	    public void insertNewNode(Node node) {
	        if (keyNum == 0)
	            max = node;
	        else {
		        node.left    = max.left;
		        max.left.right = node;
		        node.right   = max;
		        max.left       = node;
	            if (node.key > max.key)
	                max = node;
	        }

	        keyNum++;
	    }
	     	     
	    /**
	     *link node to pnode
	     *@param node
	     *@param pnode
	     */	    
	    private void link(Node node, Node pnode) {
	        node.right.left = node.left;
	        node.left.right = node.right;

	        node.parent = pnode;
	        if (pnode.child == null) {
	            pnode.child = node;
	            node.right = node;
	            node.left = node;
	        }
	        else
	        	node.left = pnode.child;
	            node.right = pnode.child.right;
	            pnode.child.right = node;
	            node.right.left = node;

	        pnode.degree++;
	        node.marked = false;
	    }
	     
	    /**
	     * after remove max node, pairwise trees
	     */
    private void pairwise() {
	        // log2(keyNum),get the max degree
	        int maxDegree = (int) Math.ceil(Math.log(keyNum) / Math.log(2.0)) + 1;
	        ArrayList<Node> con = new ArrayList<Node>(maxDegree);

	        for (int i = 0; i < maxDegree ; i++) {
	            con.add(null);
	        }
	        
	        int numOfRoot = 0;
	        Node x = max;
            if (x != null) {
                numOfRoot++;
                x = x.right;
                
                while (x != max) {
                    numOfRoot++;
                    x = x.right;
                }
            }
	        // combine the node which is the same degree
	        while (numOfRoot > 0) {
	            Node next = x.right;

	            int d = x.degree;                     // get the max degree
	            // cons[d] != null,means the degree is the same
	            while (con.get(d) != null) {
	                Node y = con.get(d);                // degree y equals degree x 
	                if (x.key < y.key) {              // guarantee key x > key y
	                    Node tmp = y;
	                    y = x;
	                    x = tmp;
	                }

	                link(y, x);    // link y to x
	                con.set(d, null);
	                d++;
	            }
	            con.set(d, x);
	            x = next;
	            numOfRoot--;
	        }
	        max = null;
	     
	        // add nodes of con[] to the root
	        for (int i=0; i < maxDegree; i++) {
	        	Node n = con.get(i);
	            if (n != null) {
	                if (max == null) {
	                    max = n;
	                }
	                else {
	                    n.left.right = n.right;
	                    n.right.left = n.left;
	                    n.left = max;
	                    n.right = max.right;
	                    max.right = n;
	                    n.right.left = n;
	                    if (n.key > max.key)
	                        max = n;
	                }
	            }
	        }
	    }
	     
	    /**
	     * remove the max node
	     */
	    public void removeMax() {

	        Node theMax = max;
	        // add child of max to Fibonacci Heap
	        while (theMax.child != null) {
	            Node child = theMax.child;

	            removeNode(child);
	            if (child.right == child)
	            	theMax.child = null;
	            else
	            	theMax.child = child.right;

		        child.left     = max.left;
		        max.left.right = child;
		        child.right    = max;
		        max.left       = child;
	            
	            child.parent = null;
	        }

	        removeNode(theMax);

	        if (theMax.right == theMax)
	            max = null;
	        else {
	            max = theMax.right;
	            pairwise();
	        }
	        keyNum--;

	        theMax = null;
	    }

	    /**
	     * get the max key
	     * @return max.key
	     */
	    public int getMaxKey() {
	        if (max == null)
	            return -1;

	        return max.key;
	    }
	     
	    /**
	     * cut node x from y, and link x to the root
	     * @param Node x
	     * @param Node y
	     */
	    private void cut(Node x, Node y) {
	        // remove x from childlist of y and decrement degree[y]
	        x.left.right = x.right;
	        x.right.left = x.left;
	        y.degree--;

	        // reset y.child if necessary
	        if (y.child == x) {
	            y.child = x.right;
	        }

	        if (y.degree == 0) {
	            y.child = null;
	        }

	        // add x to root list of heap
	        x.left = max;
	        x.right = max.right;
	        max.right = x;
	        x.right.left = x;

	        // set parent[x] to nil
	        x.parent = null;

	        // set mark[x] to false
	        x.marked = false;
	    }

	    /**
	     * do cascadingcut to the node
	     *@param node	        
	     */
	    private void cascadingCut(Node node) {
	        Node parent = node.parent;

	        if (parent != null) {
	            if (node.marked == false) 
	                node.marked = true;
	            else {
	                cut(node, parent);
	                cascadingCut(parent);
	            }
	        }
	    }

	    /** 
	     * increase the key of node to a new one
	     * @param p
	     * @param key
	     */
	    public void increaseKey(Node p, int key) {
	        if (max==null ||p==null) 
	            return ;

	        if (key < p.key) {
	            return ;
	        }

           Node parent = p.parent;
	        p.key = key;
	        if (parent!=null && (p.key > parent.key)) {
	            // set node apart from its parent and add node to the root list
	            cut(p, parent);
	            cascadingCut(parent);
	        }
	        // update the max node
	        if (p.key > max.key)
	            max = p;
	    }
	    
	    /** 
	     * get the max node of FibnacciHeap
	     * @return
	     */ 
	    public Node getMaxNode() {
	    	return max;
	    }
	    
	}
