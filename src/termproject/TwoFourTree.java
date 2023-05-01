package termproject;

import java.util.Random;

/**
 * Title:        Term Project 2-4 Trees
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Caleb Willson and Joshua Cappella
 * @version 1.0
 */
public class TwoFourTree implements Dictionary {

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
     * @exception InvalidObjectException if the key is not comparable by the comparator
     */
    public Object findElement(Object key) throws InvalidObjectException {
        if (!treeComp.isComparable(key)) {
            throw new InvalidObjectException("Invalid key given");
        }

        TFNode currNode = root();

        while (currNode != null) {
            for (int i = 0; i < currNode.getNumItems(); i++) {
                if (treeComp.isEqual(currNode.getItem(i).key(), key)) {
                    return currNode.getItem(i).element();
                }
            }

            int nextInd = findFirstGreaterThanOrEqual(currNode, key);
            currNode = currNode.getChild(nextInd);
        }

        return null;
    }

    /**
     * Inserts provided element into the Dictionary
     * @param key of object to be inserted
     * @param element to be inserted
     * @exception InvalidObjectException if the key is not comparable by the comparator
     */
    public void insertElement(Object key, Object element) throws InvalidObjectException {
        if (!treeComp.isComparable(key)) {
            throw new InvalidObjectException("Invalid key given");
        }

        TFNode currNode = root();

        // create a root if the tree is empty
        if (root() == null) {
            setRoot(new TFNode());
            root().addItem(0, new Item(key, element));
        }
        else {
            // find the leaf to insert in
            int nextInd = findFirstGreaterThanOrEqual(currNode, key);
            while (currNode.getChild(nextInd) != null) {
                currNode = currNode.getChild(nextInd);
                nextInd = findFirstGreaterThanOrEqual(currNode, key);
            }

            // insert and fix overflow
            currNode.insertItem(nextInd, new Item(key, element));

            if (currNode.getNumItems() > 3) {
                fixOverflow(currNode);
            }
        }
        
        size++;
    }

    /**
     * Searches dictionary to determine if key is present, then
     * removes and returns corresponding object
     * @param key of data to be removed
     * @return object corresponding to key
     * @exception ElementNotFoundException if the key is not in dictionary
     * @exception InvalidObjectException if the key is not comparable by the comparator
     */
    public Object removeElement(Object key) throws ElementNotFoundException, InvalidObjectException {
        if (!treeComp.isComparable(key)) {
            throw new InvalidObjectException("Invalid key given");
        }

        TFNode currNode = root();
        int currInd = -1;

        // find the item to remove
        boolean found = false;
        while (currNode != null && !found) {
            for (currInd = 0; currInd < currNode.getNumItems(); currInd++) {
                if (treeComp.isEqual(currNode.getItem(currInd).key(), key)) {
                    found = true;
                    break;
                }
            }
            if (found) { break; }

            int nextInd = findFirstGreaterThanOrEqual(currNode, key);
            currNode = currNode.getChild(nextInd);
        }
        if (currNode == null) {
            throw new ElementNotFoundException("Key not found");
        } 

        // remove and replace with the in order successor
        Object removedElement = null;
        // the node is a leaf
        if (currNode.getChild(0) == null) {
            removedElement = currNode.removeItem(currInd).element();

            if (currNode.getNumItems() == 0) {
                fixUnderflow(currNode);
            }
        }
        // the node has an in order successor
        else {
            TFNode successor = currNode.getChild(currInd + 1);
            while (successor != null && successor.getChild(0) != null) {
                successor = successor.getChild(0);
            }

            removedElement = currNode.deleteItem(currInd).element();
            currNode.addItem(currInd, successor.removeItem(0));

            if (successor.getNumItems() == 0) {
                fixUnderflow(successor);
            }
        }

        size--;
        return removedElement;
    }

    /**
     * Private helper method used by insertElement to fix an overflowed node
     * @param node - the overflowed node
     */
    private void fixOverflow(TFNode node) {
        if (node.getParent() == null) {
            TFNode newRoot = new TFNode();
            node.setParent(newRoot);
            newRoot.setChild(0, node);
            setRoot(newRoot);

            splitNode(node, 0);
        }
        else {
            int parentIndex = whichChild(node);
            TFNode parent = node.getParent();

            splitNode(node, parentIndex);

            if (parent.getNumItems() > 3) {
                fixOverflow(parent);
            }
        }
    }

    /**
     * Private helper method that takes an overflowed node and splits it in two
     * @param node - the overflowed node
     * @param parentInd - the node's index in its parent node
     */
    private void splitNode(TFNode node, int parentInd) {
        TFNode split = new TFNode();

        split.setParent(node.getParent());
        split.insertItem(0, node.deleteItem(3));
        node.getParent().insertItem(parentInd, node.deleteItem(2));
        node.getParent().setChild(parentInd + 1, split);

        split.setChild(0, node.getChild(3));
        split.setChild(1, node.getChild(4));
        node.setChild(3, null);
        node.setChild(4, null);

        if (split.getChild(0) != null) {
            split.getChild(0).setParent(split);
        }
        if (split.getChild(1) != null) {
            split.getChild(1).setParent(split);
        }
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
     * Private helper method used by removeElement used to fix an
     * underflowed node.
     * @param node - the underflowed node
     */
    private void fixUnderflow(TFNode node) {
        if (node != root()) {
            int parentInd = whichChild(node);
            TFNode parent = node.getParent();
            TFNode leftSib = (parentInd > 0) ? parent.getChild(parentInd - 1) : null;
            TFNode rightSib = (parentInd < 3) ? parent.getChild(parentInd + 1) : null;

            // left transfer
            if (leftSib != null && leftSib.getNumItems() > 1) {
                node.insertItem(0, parent.deleteItem(parentInd - 1));

                node.setChild(0, leftSib.getChild(leftSib.getNumItems()));
                if (leftSib.getChild(leftSib.getNumItems()) != null) {
                    leftSib.getChild(leftSib.getNumItems()).setParent(node);
                }

                leftSib.setChild(leftSib.getNumItems(), null);
                parent.addItem(parentInd - 1, leftSib.deleteItem(leftSib.getNumItems() - 1));
            }
            // right transfer
            else if (rightSib != null && rightSib.getNumItems() > 1) {
                node.insertItem(0, parent.deleteItem(parentInd));

                node.setChild(1, rightSib.getChild(0));
                if (rightSib.getChild(0) != null) {
                    rightSib.getChild(0).setParent(node);
                }

                parent.addItem(parentInd, rightSib.removeItem(0));
            }
            // left fusion
            else if (leftSib != null) {
                TFNode lostChild = node.getChild(0);

                leftSib.insertItem(leftSib.getNumItems(), parent.removeItem(parentInd - 1));
                leftSib.setChild(leftSib.getNumItems(), lostChild);
                if (lostChild != null) {
                    lostChild.setParent(leftSib);
                }
                parent.setChild(parentInd - 1, leftSib);

                if (parent.getNumItems() == 0) {
                    fixUnderflow(parent);
                }
            }
            // right fusion
            else {
                TFNode lostChild = node.getChild(0);

                rightSib.insertItem(0, parent.removeItem(parentInd));
                rightSib.setChild(0, lostChild);
                if (lostChild != null) {
                    lostChild.setParent(rightSib);
                }
                parent.setChild(parentInd, rightSib);

                if (parent.getNumItems() == 0) {
                    fixUnderflow(parent);
                }
            }
        }
        // special root case
        else {
            setRoot(root().getChild(0));
            if (root() != null) {
                root().setParent(null);
            }
        }
    }

    /**
     * main method used for testing the TFTree. Runs a small scale fixed test
     * and a large scale randomized test.
     * @param args
     */
    public static void main(String[] args) {
        Comparator myComp = new IntegerComparator();
        TwoFourTree myTree = new TwoFourTree(myComp);
        try{

        System.out.println("Beginning small scale fixed test...");
        myTree.insertElement(19, 19);
        myTree.insertElement(45, 45);
        myTree.insertElement(63, 63);
        myTree.insertElement(28, 28);
        myTree.insertElement(10, 10);
        myTree.insertElement(67, 67);
        myTree.insertElement(45, 45);
        myTree.insertElement(1, 1);
        myTree.insertElement(97, 97);
        myTree.insertElement(25, 25);
        myTree.insertElement(26, 26);
        myTree.insertElement(100, 100);

        myTree.printAllElements();
        myTree.checkTree();
        System.out.println("Tree size: " + myTree.size());

        Object test1 = myTree.findElement(45);
        System.out.println("Searched key 45: " + test1);
        Object test2 = myTree.findElement(69);
        System.out.println("Searched key 69: " + test2);
        Object test3 = myTree.findElement(63);
        System.out.println("Searched key 63: " + test3);
        Object test4 = myTree.findElement(101);
        System.out.println("Searched key 101: " + test4);
        Object test5 = myTree.findElement(1);
        System.out.println("Searched key 1: " + test5);

        System.out.println("Removing some elements...");
        myTree.removeElement(10);
        myTree.removeElement(45);
        myTree.removeElement(25);
        myTree.removeElement(63);

        myTree.printAllElements();
        myTree.checkTree();

        System.out.println("Beginning large scale random test...");
        myTree = new TwoFourTree(myComp);
        Random master = new Random();
        long randomSeed = master.nextInt(1000000000);
        Random rng = new Random(randomSeed);
        final int TEST_SIZE = 100000000;

        long startTime = System.nanoTime();
        
        System.out.println("Inserting " + TEST_SIZE + " elements...");
        for (int i = 0; i < TEST_SIZE; i++) {
            int nextRand = rng.nextInt(TEST_SIZE / 10);
            myTree.checkTree();
            myTree.insertElement(nextRand, nextRand);
            if (myTree.size() % (TEST_SIZE / 50) == 0) {
                System.out.print(".");
            }
        }

        rng = new Random(randomSeed);
        System.out.println("\nInserting complete. Removing all elements...");
        while (!myTree.isEmpty()) {
            int nextRand = rng.nextInt(TEST_SIZE / 10);
            int removedItem = (Integer)myTree.removeElement(nextRand);
            myTree.checkTree();
            if (nextRand != removedItem) {
                throw new TFNodeException("Invalid item removed");
                //System.out.println("Incorrect item removed. Expected " + nextRand + " removed " + removedItem + ".");
                //return;
            }

            if (myTree.size() % (TEST_SIZE / 50) == 0) {
                System.out.print(".");
            }
            if (myTree.size() < 30) {
                myTree.printAllElements();
                System.out.println("----------------------");
            }
        }
        
        System.out.println("\nRemoval complete.");
        
        long endTime = System.nanoTime();
        long totalTime = (endTime - startTime) / 1000000;

        System.out.println("Total time: " + totalTime + " ms");
        }
        catch (InvalidObjectException e) {}
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
