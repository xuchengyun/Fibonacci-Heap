
public class Node {
    int key;               // the key
    int degree;            // degree
    Node left;             // left sibling
    Node right;            // right sibling
    Node child;            // child
    Node parent;           // parent
    boolean marked;        // whether a child is deleted
    String hashtag;        // hashtage

    public Node(int key,String hashtag) {
        this.key    = key;
        this.degree = 0;
        this.marked = false;
        this.left   = this;
        this.right  = this;
        this.parent = null;
        this.child  = null;
        this.hashtag = hashtag;
        }
    }