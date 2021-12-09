import java.util.*;
public class MyQueue<T> {
  LinkedList<T> t;
  MyQueue() {
      t = new LinkedList<T>();
    }
  void clear() {
      t.clear();
    }
  boolean isEmpty() {
      return(t.isEmpty());
    }
  void push(T x) {
      t.addLast(x);
  }
  T pop() { 
    if(isEmpty()) return(null);
      return(t.removeFirst());
    }
  T top() { 
    if(isEmpty()) return(null);
    return(t.getFirst());
   }  
}
