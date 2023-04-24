package termproject;

/**
 * Title:        Term Project 2-4 Trees
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class TwoFourTree
        implements Dictionary {

    private Comparator treeComp;
    private int size = 0;
    private TFNode treeRoot = null;

    public TwoFourTree(Comparator comp) {
        treeComp = comp;
    }

    private TFNode root() {
        return treeRoot;
    }

    private void setRoot(TFNode root) {
        treeRoot = root;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Searches dictionary to determine if key is present
     * @param key to be searched for
     * @return object corresponding to key; null if not found
     */
    public Object findElement(Object key) {
        return null;
    }

    /**
     * Inserts provided element into the Dictionary
     * @param key of object to be inserted
     * @param element to be inserted
     */
    public void insertElement(Object key, Object element) {
    }

    /**
     * Private helper method to find the first key greater than or equal to a 
     * given key in a node
     * @param node - the node to be searched
     * @param key - the key to search for
     * @return the index of the first key greater than or equal to the given key
     */
    private int findFirstGreaterThanOrEqual(TFNode node, Object key) {
        int i = 0;
        for (i = 0; i < node.getNumItems(); i++) {
            if (treeComp.isGreaterThanOrEqualTo(node.getItem(i).key(), key)) {
                break;
            }
        }
        return i;
    }

    /**
     * Private helper method which gives the position in a node's parent's
     * child array of a given node.
     * @param node - the ndoe to be checked
     * @return the index of that node in its parent's child array
     */
    private int whichChild(TFNode node) {
        TFNode parent = node.getParent();

        int i;
        for (i = 0; i <= parent.getNumItems(); i++) {
            if (parent.getChild(i) == node) {
                break;
            }
        }
        return i;
    }

    /**
     * Searches dictionary to determine if key is present, then
     * removes and returns corresponding object
     * @param key of data to be removed
     * @return object corresponding to key
     * @exception ElementNotFoundException if the key is not in dictionary
     */
    public Object removeElement(Object key) throws ElementNotFoundException {
        return null;
    }

    public static void main(String[] args) {
        Comparator myComp = new IntegerComparator();
        TwoFourTree myTree = new TwoFourTree(myComp);

        Integer myInt1 = 47;
        myTree.insertElement(myInt1, myInt1);
        //myTree.printAllElements();

        Integer myInt2 = 83;
        myTree.insertElement(myInt2, myInt2);
        //myTree.printAllElements();

        Integer myInt3 = 22;
        myTree.insertElement(myInt3, myInt3);
        //myTree.printAllElements();

        Integer myInt4 = 16;
        myTree.insertElement(myInt4, myInt4);
        //myTree.printAllElements();

        Integer myInt5 = 49;
        myTree.insertElement(myInt5, myInt5);
        //myTree.printAllElements();

        Integer myInt6 = 100;
        myTree.insertElement(myInt6, myInt6);
        //myTree.printAllElements();

        Integer myInt7 = 38;
        myTree.insertElement(myInt7, myInt7);
        //myTree.printAllElements();

        Integer myInt8 = 3;
        myTree.insertElement(myInt8, myInt8);
        //myTree.printAllElements();

        Integer myInt9 = 53;
        myTree.insertElement(myInt9, myInt9);
        //myTree.printAllElements();

        Integer myInt10 = 66;
        myTree.insertElement(myInt10, myInt10);
        //myTree.printAllElements();

        Integer myInt11 = 19;
        myTree.insertElement(myInt11, myInt11);
        //myTree.printAllElements();

        Integer myInt12 = 23;
        myTree.insertElement(myInt12, myInt12);
        //myTree.printAllElements();

        Integer myInt13 = 24;
        myTree.insertElement(myInt13, myInt13);
        //myTree.printAllElements();

        Integer myInt14 = 88;
        myTree.insertElement(myInt14, myInt14);
        //myTree.printAllElements();

        Integer myInt15 = 1;
        myTree.insertElement(myInt15, myInt15);
        //myTree.printAllElements();

        Integer myInt16 = 97;
        myTree.insertElement(myInt16, myInt16);
        //myTree.printAllElements();

        Integer myInt17 = 94;
        myTree.insertElement(myInt17, myInt17);
        //myTree.printAllElements();

        Integer myInt18 = 35;
        myTree.insertElement(myInt18, myInt18);
        //myTree.printAllElements();

        Integer myInt19 = 51;
        myTree.insertElement(myInt19, myInt19);
        //myTree.printAllElements();

        myTree.printAllElements();
        System.out.println("done");

        myTree = new TwoFourTree(myComp);
        final int TEST_SIZE = 10000;


        for (int i = 0; i < TEST_SIZE; i++) {
            myTree.insertElement(i, i);
            //          myTree.printAllElements();
            //         myTree.checkTree();
        }
        System.out.println("removing");
        for (int i = 0; i < TEST_SIZE; i++) {
            int out = (Integer) myTree.removeElement(i);
            if (out != i) {
                throw new TwoFourTreeException("main: wrong element removed");
            }
            if (i > TEST_SIZE - 15) {
                myTree.printAllElements();
            }
        }
        System.out.println("done");
    }

    public void printAllElements() {
        int indent = 0;
        if (root() == null) {
            System.out.println("The tree is empty");
        }
        else {
            printTree(root(), indent);
        }
    }

    public void printTree(TFNode start, int indent) {
        if (start == null) {
            return;
        }
        for (int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
        printTFNode(start);
        indent += 4;
        int numChildren = start.getNumItems() + 1;
        for (int i = 0; i < numChildren; i++) {
            printTree(start.getChild(i), indent);
        }
    }

    public void printTFNode(TFNode node) {
        int numItems = node.getNumItems();
        for (int i = 0; i < numItems; i++) {
            System.out.print(((Item) node.getItem(i)).element() + " ");
        }
        System.out.println();
    }

    // checks if tree is properly hooked up, i.e., children point to parents
    public void checkTree() {
        checkTreeFromNode(treeRoot);
    }

    private void checkTreeFromNode(TFNode start) {
        if (start == null) {
            return;
        }

        if (start.getParent() != null) {
            TFNode parent = start.getParent();
            int childIndex = 0;
            for (childIndex = 0; childIndex <= parent.getNumItems(); childIndex++) {
                if (parent.getChild(childIndex) == start) {
                    break;
                }
            }
            // if child wasn't found, print problem
            if (childIndex > parent.getNumItems()) {
                System.out.println("Child to parent confusion");
                printTFNode(start);
            }
        }

        if (start.getChild(0) != null) {
            for (int childIndex = 0; childIndex <= start.getNumItems(); childIndex++) {
                if (start.getChild(childIndex) == null) {
                    System.out.println("Mixed null and non-null children");
                    printTFNode(start);
                }
                else {
                    if (start.getChild(childIndex).getParent() != start) {
                        System.out.println("Parent to child confusion");
                        printTFNode(start);
                    }
                    for (int i = childIndex - 1; i >= 0; i--) {
                        if (start.getChild(i) == start.getChild(childIndex)) {
                            System.out.println("Duplicate children of node");
                            printTFNode(start);
                        }
                    }
                }

            }
        }

        int numChildren = start.getNumItems() + 1;
        for (int childIndex = 0; childIndex < numChildren; childIndex++) {
            checkTreeFromNode(start.getChild(childIndex));
        }

    }
}
