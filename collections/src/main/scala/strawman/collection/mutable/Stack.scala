package strawman.collection.mutable

import scala.{AnyRef, Array, Boolean, Int, SerialVersionUID, Serializable}
import strawman.collection.{IterableOnce, SeqFactory, StrictOptimizedSeqFactory, StrictOptimizedSeqOps, toNewSeq}

/** A stack implements a data structure which allows to store and retrieve
  *  objects in a last-in-first-out (LIFO) fashion.
  *
  *  @tparam A    type of the elements contained in this stack.
  *
  *  @author  Pathikrit Bhowmick
  *  @version 2.13
  *  @since   2.13
  *
  *  @define Coll `Stack`
  *  @define coll stack
  *  @define orderDependent
  *  @define orderDependentFold
  *  @define mayNotTerminateInf
  *  @define willNotTerminateInf
  */
@SerialVersionUID(3L)
class Stack[A] protected (array: Array[AnyRef], start: Int, end: Int)
  extends ArrayDeque[A](array, start, end)
    with IndexedSeqOps[A, Stack, Stack[A]]
    with StrictOptimizedSeqOps[A, Stack, Stack[A]]
    with Cloneable[Stack[A]]
    with Serializable {

  def this(initialSize: Int = ArrayDeque.DefaultInitialSize) =
    this(ArrayDeque.alloc(initialSize), start = 0, end = 0)

  override def iterableFactory: SeqFactory[Stack] = Stack

  /**
    * Add elements to the top of this stack
    *
    * @param elem
    * @return
    */
  def push(elem: A): this.type = this += elem

  /** Push two or more elements onto the stack. The last element
    *  of the sequence will be on top of the new stack.
    *
    *  @param   elems      the element sequence.
    *  @return the stack with the new elements on top.
    */
  def push(elem1: A, elem2: A, elems: A*): this.type = push(elem1).push(elem2).pushAll(elems.toStrawman)

  /** Push all elements in the given traversable object onto the stack. The
    *  last element in the traversable object will be on top of the new stack.
    *
    *  @param elems the traversable object.
    *  @return the stack with the new elements on top.
    */
  def pushAll(elems: strawman.collection.IterableOnce[A]): this.type = this ++= elems

  /**
    * Removes the top element from this stack and return it
    *
    * @return
    * @throws java.util.NoSuchElementException when stack is empty
    */
  def pop(): A = removeLast()

  /**
    * Pop all elements from this stack and return it
    *
    * @return The removed elements
    */
  def popAll(): strawman.collection.Seq[A] = removeAll()

  /**
    * Returns and removes all elements from the top of this stack which satisfy the given predicate
    *
    *  @param f   the predicate used for choosing elements
    *  @return The removed elements
    */
  def popWhile(f: A => Boolean): strawman.collection.Seq[A] = removeLastWhile(f)

  override def clone(): Stack[A] = {
    val bf = newSpecificBuilder()
    bf ++= this
    bf.result()
  }

  override protected def ofArray(array: Array[AnyRef], end: Int): Stack[A] =
    new Stack(array, start = 0, end)

}

/**
  * $factoryInfo
  * @define coll stack
  * @define Coll `Stack`
  */
object Stack extends StrictOptimizedSeqFactory[Stack] {

  def from[A](source: IterableOnce[A]): Stack[A] = empty ++= source

  def empty[A]: Stack[A] = new Stack

  def newBuilder[A](): Builder[A, Stack[A]] = new GrowableBuilder[A, Stack[A]](empty)

}