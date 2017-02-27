#从源码角度来看Android事件分发
事件分发顾名思义，意思就是当你点击一块区域之后，Android内部代码究竟经历了啥心历路程（这背后到底是人性的扭曲还是道德的沦丧），才把事件传到该点击的控件。其实就是我们看到的每个应用界面都被一个叫DecorView的FramLayout包裹着，当点击activity调用他的dispatchTouchEvent方法把事件传递给DecorView，然后DecorView去遍历他的子View向下分发，下发到最底层View的时候再往上响应,这就是大概的整个过程。但是还是需要从源码的角度去了解他们的细节处理:
DecorView他继承的是FramLayout是一个ViewGroup，所以先去查看ViewGroup的dispatchTouchEvent 源码如下：

```
public boolean dispatchTouchEvent(MotionEvent ev) {  
        //验证事件是否连续  
        if (mInputEventConsistencyVerifier != null) {  
            mInputEventConsistencyVerifier.onTouchEvent(ev, 1);  
        }  
        //这个变量用于记录事件是否被处理完  
        boolean handled = false;  
        //过滤掉一些不合法的事件：当前的View的窗口被遮挡了。  
        if (onFilterTouchEventForSecurity(ev)) {  
            //如果事件发生的View在的窗口，没有被遮挡  
            final int action = ev.getAction();  
            //重置前面为0 ，只留下后八位，用于判断相等时候，可以提高性能。  
            final int actionMasked = action & MotionEvent.ACTION_MASK;  
            //判断是不是Down事件，如果是的话，就要做初始化操作  
            if (actionMasked == MotionEvent.ACTION_DOWN) {  
                //如果是down事件，就要清空掉之前的状态，比如,重置手势判断什么的。  
                //比如，之前正在判断是不是一个单点的滑动，但是第二个down来了，就表示，不可能是单点的滑动，要重新开始判断触摸的手势  
                //清空掉mFirstTouchTarget  
                // Throw away all previous state when starting a new touch gesture.  
                // The framework may have dropped the up or cancel event for the previous gesture  
                // due to an app switch, ANR, or some other state change.  
                cancelAndClearTouchTargets(ev);  
                resetTouchState();  
            }  
  
   
            //检查是否拦截事件  
            final boolean intercepted;  
            //如果当前是Down事件，或者已经有处理Touch事件的目标了  
            if (actionMasked == MotionEvent.ACTION_DOWN  
                    || mFirstTouchTarget != null) {  
                //判断允不允许这个View拦截  
                //使用与运算作为判断，可以让我们在flag中，存储好几个标志  
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;  
                //如果说允许拦截事件  
                if (!disallowIntercept) {  
                    //确定是不是拦截了  
                    intercepted = onInterceptTouchEvent(ev);  
                    //重新恢复Action，以免action在上面的步骤被人为地改变了  
                    ev.setAction(action); // restore action in case it was changed  
                } else {  
                    intercepted = false;  
                }  
            } else {  
                // There are no touch targets and this action is not an initial down  
                // so this view group continues to intercept touches.  
                //如果说，事件已经初始化过了，并且没有子View被分配处理，那么就说明，这个ViewGroup已经拦截了这个事件  
                intercepted = true;  
            }  
  
            // Check for cancelation.  
            //如果viewFlag被设置了PFLAG_CANCEL_NEXT_UP_EVENT ,那么就表示，下一步应该是Cancel事件  
            //或者如果当前的Action为取消，那么当前事件应该就是取消了。  
            final boolean canceled = resetCancelNextUpFlag(this)  
                    || actionMasked == MotionEvent.ACTION_CANCEL;  
  
            // Update list of touch targets for pointer down, if needed.  
            //如果需要（不是取消，也没有被拦截）的话，那么在触摸down事件的时候更新触摸目标列表  
            //split代表，当前的ViewGroup是不是支持分割MotionEvent到不同的View当中  
            final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0;  
            //新的触摸对象，  
            TouchTarget newTouchTarget = null;  
            //是否把事件分配给了新的触摸  
            boolean alreadyDispatchedToNewTouchTarget = false;  
            //事件不是取消事件，也没有拦截那么就要判断  
            if (!canceled && !intercepted) {  
                //如果是个全新的Down事件  
                //或者是有新的触摸点  
                //或者是光标来回移动事件（不太明白什么时候发生）  
                if (actionMasked == MotionEvent.ACTION_DOWN  
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)  
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {  
                    //这个事件的索引，也就是第几个事件，如果是down事件就是0  
                    final int actionIndex = ev.getActionIndex(); // always 0 for down  
                    //获取分配的ID的bit数量  
                    final int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex)  
                            : TouchTarget.ALL_POINTER_IDS;  
  
                    // Clean up earlier touch targets for this pointer id in case they  
                    // have become out of sync.  
                    //清理之前触摸这个指针标识,以防他们的目标变得不同步。  
                    removePointersFromTouchTargets(idBitsToAssign);  
  
                    final int childrenCount = mChildrenCount;  
                    //如果新的触摸对象为null（这个不是铁定的吗）并且当前ViewGroup有子元素  
                    if (newTouchTarget == null && childrenCount != 0) {  
                        final float x = ev.getX(actionIndex);  
                        final float y = ev.getY(actionIndex);  
                        // Find a child that can receive the event.  
                        // Scan children from front to back.  
                        //下面所做的工作，就是找到可以接收这个事件的子元素  
                        final View[] children = mChildren;  
                        //是否使用自定义的顺序来添加控件  
                        final boolean customOrder = isChildrenDrawingOrderEnabled();  
                        for (int i = childrenCount - 1; i >= 0; i--) {  
                            //如果是用了自定义的顺序来添加控件，那么绘制的View的顺序和mChildren的顺序是不一样的  
                            //所以要根据getChildDrawingOrder取出真正的绘制的View  
                            //自定义的绘制，可能第一个会画到第三个，和第四个，第二个画到第一个，这样里面的内容和Children是不一样的  
                            final int childIndex = customOrder ?  
                                    getChildDrawingOrder(childrenCount, i) : i;  
                            final View child = children[childIndex];  
                            //如果child不可以接收这个触摸的事件，或者触摸事件发生的位置不在这个View的范围内  
                            if (!canViewReceivePointerEvents(child)  
                                    || !isTransformedTouchPointInView(x, y, child, null)) {  
                                continue;  
                            }  
                            //获取新的触摸对象，如果当前的子View在之前的触摸目标的列表当中就返回touchTarget  
                            //子View不在之前的触摸目标列表那么就返回null  
                            newTouchTarget = getTouchTarget(child);  
                            if (newTouchTarget != null) {  
                                // Child is already receiving touch within its bounds.  
                                // Give it the new pointer in addition to the ones it is handling.  
                                //如果新的触摸目标对象不为空，那么就把这个触摸的ID赋予它，这样子，  
                                //这个触摸的目标对象的id就含有了好几个pointer的ID了  
  
                                newTouchTarget.pointerIdBits |= idBitsToAssign;  
                                break;  
                            }  
                            //如果子View不在之前的触摸目标列表中，先重置childView的标志，去除掉CACEL的标志  
                            resetCancelNextUpFlag(child);  
                            //调用子View的dispatchTouchEvent,并且把pointer的id 赋予进去  
                            //如果说，子View接收并且处理了这个事件，那么就更新上一次触摸事件的信息，  
                            //并且为创建一个新的触摸目标对象，并且绑定这个子View和Pointer的ID  
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {  
                                // Child wants to receive touch within its bounds.  
                                mLastTouchDownTime = ev.getDownTime();  
                                mLastTouchDownIndex = childIndex;  
                                mLastTouchDownX = ev.getX();  
                                mLastTouchDownY = ev.getY();  
                                newTouchTarget = addTouchTarget(child, idBitsToAssign);  
                                alreadyDispatchedToNewTouchTarget = true;  
                                break;  
                            }  
                        }  
                    }  
                    //如果newTouchTarget为null，就代表，这个事件没有找到子View去处理它，  
                    //那么，如果之前已经有了触摸对象（比如，我点了一张图，另一个手指在外面图的外面点下去）  
                    //那么就把这个之前那个触摸目标定为第一个触摸对象，并且把这个触摸（pointer）分配给最近添加的触摸目标  
                    if (newTouchTarget == null && mFirstTouchTarget != null) {  
                        // Did not find a child to receive the event.  
                        // Assign the pointer to the least recently added target.  
                        newTouchTarget = mFirstTouchTarget;  
                        while (newTouchTarget.next != null) {  
                            newTouchTarget = newTouchTarget.next;  
                        }  
                        newTouchTarget.pointerIdBits |= idBitsToAssign;  
                    }  
                }  
            }  
  
            // Dispatch to touch targets.  
            //如果没有触摸目标  
            if (mFirstTouchTarget == null) {  
                // No touch targets so treat this as an ordinary view.  
                //那么就表示我们要自己在这个ViewGroup处理这个触摸事件了  
                handled = dispatchTransformedTouchEvent(ev, canceled, null,  
                        TouchTarget.ALL_POINTER_IDS);  
            } else {  
                // Dispatch to touch targets, excluding the new touch target if we already  
                // dispatched to it.  Cancel touch targets if necessary.  
                TouchTarget predecessor = null;  
                TouchTarget target = mFirstTouchTarget;  
                //遍历TouchTargt树.分发事件，如果我们已经分发给了新的TouchTarget那么我们就不再分发给newTouchTarget  
                while (target != null) {  
                    final TouchTarget next = target.next;  
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {  
                        handled = true;  
                    } else {  
                        //是否让child取消处理事件，如果为true，就会分发给child一个ACTION_CANCEL事件  
                        final boolean cancelChild = resetCancelNextUpFlag(target.child)  
                                || intercepted;  
                        //派发事件  
                        if (dispatchTransformedTouchEvent(ev, cancelChild,  
                                target.child, target.pointerIdBits)) {  
                            handled = true;  
                        }  
                        //cancelChild也就是说，派发给了当前child一个ACTION_CANCEL事件，  
                        //那么就移除这个child  
                        if (cancelChild) {  
                            //没有父节点，也就是当前是第一个TouchTarget  
                            //那么就把头去掉  
                            if (predecessor == null) {  
                                mFirstTouchTarget = next;  
                            } else {  
                                //把下一个赋予父节点的上一个，这样当前节点就被丢弃了  
                                predecessor.next = next;  
                            }  
                            //回收内存  
                            target.recycle();  
                            //把下一个赋予现在  
                            target = next;  
                            //下面的两行不执行了，因为我们已经做了链表的操作了。  
                            //主要是我们不能执行predecessor=target，因为删除本节点的话，父节点还是父节点  
                            continue;  
                        }  
                    }  
                    //如果没有删除本节点，那么下一轮父节点就是当前节点，下一个节点也是下一轮的当前节点  
                    predecessor = target;  
                    target = next;  
                }  
            }  
  
            // Update list of touch targets for pointer up or cancel, if needed.  
            //遇到了取消事件、或者是单点触摸下情况下手指离开，我们就要更新触摸的状态  
            if (canceled  
                    || actionMasked == MotionEvent.ACTION_UP  
                    || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {  
                resetTouchState();  
            } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {  
                //如果是多点触摸下的手指抬起事件，就要根据idBit从TouchTarget中移除掉对应的Pointer(触摸点)  
                final int actionIndex = ev.getActionIndex();  
                final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);  
                removePointersFromTouchTargets(idBitsToRemove);  
            }  
        }  
  
        if (!handled && mInputEventConsistencyVerifier != null) {  
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);  
        }  
        return handled;  
    }  
```
可以从ViewGroup的dispatchTouchEvent方法看到，首先判断当前触摸事件的windows是否被遮盖或者被终止，如果不是的话则去获取当前的动作然后去判断拦截，拦截方法决定因素有两个,一个是mGroupFlags标记（可以由requestDisallowInterceptTouchEvent方法去改变），另外一个是自身的onInterceptTouchEvent方法去决定。如果intercepted为true，则不再向下分发，而是自己进行事件的处理（一般默认为false），为fasle的话，再判断当前事件是否为取消标记，如果都不是的话则开始向下分发事件，最顶层的view会开始遍历自身的所以子view，调用dispatchTransformedTouchEvent方法，源码如下：

```
/**
     * Transforms a motion event into the coordinate space of a particular child view,
     * filters out irrelevant pointer ids, and overrides its action if necessary.
     * If child is null, assumes the MotionEvent will be sent to this ViewGroup instead.
     */
    private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
            View child, int desiredPointerIdBits) {
        final boolean handled;

        // Canceling motions is a special case.  We don't need to perform any transformations
        // or filtering.  The important part is the action, not the contents.
        final int oldAction = event.getAction();
        if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
            event.setAction(MotionEvent.ACTION_CANCEL);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }

        // Calculate the number of pointers to deliver.
        final int oldPointerIdBits = event.getPointerIdBits();
        final int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;

        // If for some reason we ended up in an inconsistent state where it looks like we
        // might produce a motion event with no pointers in it, then drop the event.
        if (newPointerIdBits == 0) {
            return false;
        }

        // If the number of pointers is the same and we don't need to perform any fancy
        // irreversible transformations, then we can reuse the motion event for this
        // dispatch as long as we are careful to revert any changes we make.
        // Otherwise we need to make a copy.
        final MotionEvent transformedEvent;
        if (newPointerIdBits == oldPointerIdBits) {
            if (child == null || child.hasIdentityMatrix()) {
                if (child == null) {
                    handled = super.dispatchTouchEvent(event);
                } else {
                    final float offsetX = mScrollX - child.mLeft;
                    final float offsetY = mScrollY - child.mTop;
                    event.offsetLocation(offsetX, offsetY);

                    handled = child.dispatchTouchEvent(event);

                    event.offsetLocation(-offsetX, -offsetY);
                }
                return handled;
            }
            transformedEvent = MotionEvent.obtain(event);
        } else {
            transformedEvent = event.split(newPointerIdBits);
        }

        // Perform any necessary transformations and dispatch.
        if (child == null) {
            handled = super.dispatchTouchEvent(transformedEvent);
        } else {
            final float offsetX = mScrollX - child.mLeft;
            final float offsetY = mScrollY - child.mTop;
            transformedEvent.offsetLocation(offsetX, offsetY);
            if (! child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }

            handled = child.dispatchTouchEvent(transformedEvent);
        }

        // Done.
        transformedEvent.recycle();
        return handled;
    }
```
这部分代码就是做了去查看传进来的View是否有子View如果没有子View则调用surper.dispatchTouchEvent(),也就是View自身的dispatchTouchEvent，若是还有子View则调用他子View（也就是ViewGroup）的dispatchTouchEvent直到他没有子View为止。

整个事件传递就是这么简单，后面再看看事件响应。

```
    public boolean dispatchTouchEvent(MotionEvent event) {
        // If the event should be handled by accessibility focus first.
        if (event.isTargetAccessibilityFocus()) {
            // We don't have focus or no virtual descendant has it, do not handle the event.
            if (!isAccessibilityFocusedViewOrHost()) {
                return false;
            }
            // We have focus and got the event, then use normal event dispatch.
            event.setTargetAccessibilityFocus(false);
        }

        boolean result = false;

        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(event, 0);
        }

        final int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            // Defensive cleanup for new gesture
            stopNestedScroll();
        }

        if (onFilterTouchEventForSecurity(event)) {
            if ((mViewFlags & ENABLED_MASK) == ENABLED && handleScrollBarDragging(event)) {
                result = true;
            }
            //noinspection SimplifiableIfStatement
            ListenerInfo li = mListenerInfo;
            if (li != null && li.mOnTouchListener != null
                    && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }

            if (!result && onTouchEvent(event)) {
                result = true;
            }
        }

        if (!result && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }

        // Clean up after nested scrolls if this is the end of a gesture;
        // also cancel it if we tried an ACTION_DOWN but we didn't want the rest
        // of the gesture.
        if (actionMasked == MotionEvent.ACTION_UP ||
                actionMasked == MotionEvent.ACTION_CANCEL ||
                (actionMasked == MotionEvent.ACTION_DOWN && !result)) {
            stopNestedScroll();
        }

        return result;
    }
  /**
     * Implement this method to handle touch screen motion events.
     * <p>
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling
     * {@link #performClick()}. This will ensure consistent system behavior,
     * including:
     * <ul>
     * <li>obeying click sound preferences
     * <li>dispatching OnClickListener calls
     * <li>handling {@link AccessibilityNodeInfo#ACTION_CLICK ACTION_CLICK} when
     * accessibility features are enabled
     * </ul>
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final int viewFlags = mViewFlags;
        final int action = event.getAction();

        if ((viewFlags & ENABLED_MASK) == DISABLED) {
            if (action == MotionEvent.ACTION_UP && (mPrivateFlags & PFLAG_PRESSED) != 0) {
                setPressed(false);
            }
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return (((viewFlags & CLICKABLE) == CLICKABLE
                    || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)
                    || (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE);
        }
        if (mTouchDelegate != null) {
            if (mTouchDelegate.onTouchEvent(event)) {
                return true;
            }
        }

        if (((viewFlags & CLICKABLE) == CLICKABLE ||
                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE) ||
                (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE) {
            switch (action) {
                case MotionEvent.ACTION_UP:
                    boolean prepressed = (mPrivateFlags & PFLAG_PREPRESSED) != 0;
                    if ((mPrivateFlags & PFLAG_PRESSED) != 0 || prepressed) {
                        // take focus if we don't have it already and we should in
                        // touch mode.
                        boolean focusTaken = false;
                        if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                            focusTaken = requestFocus();
                        }

                        if (prepressed) {
                            // The button is being released before we actually
                            // showed it as pressed.  Make it show the pressed
                            // state now (before scheduling the click) to ensure
                            // the user sees it.
                            setPressed(true, x, y);
                       }

                        if (!mHasPerformedLongPress && !mIgnoreNextUpEvent) {
                            // This is a tap, so remove the longpress check
                            removeLongPressCallback();

                            // Only perform take click actions if we were in the pressed state
                            if (!focusTaken) {
                                // Use a Runnable and post this rather than calling
                                // performClick directly. This lets other visual state
                                // of the view update before click actions start.
                                if (mPerformClick == null) {
                                    mPerformClick = new PerformClick();
                                }
                                if (!post(mPerformClick)) {
                                    performClick();
                                }
                            }
                        }

                        if (mUnsetPressedState == null) {
                            mUnsetPressedState = new UnsetPressedState();
                        }

                        if (prepressed) {
                            postDelayed(mUnsetPressedState,
                                    ViewConfiguration.getPressedStateDuration());
                        } else if (!post(mUnsetPressedState)) {
                            // If the post failed, unpress right now
                            mUnsetPressedState.run();
                        }

                        removeTapCallback();
                    }
                    mIgnoreNextUpEvent = false;
                    break;

                case MotionEvent.ACTION_DOWN:
                    mHasPerformedLongPress = false;

                    if (performButtonActionOnTouchDown(event)) {
                        break;
                    }

                    // Walk up the hierarchy to determine if we're inside a scrolling container.
                    boolean isInScrollingContainer = isInScrollingContainer();

                    // For views inside a scrolling container, delay the pressed feedback for
                    // a short period in case this is a scroll.
                    if (isInScrollingContainer) {
                        mPrivateFlags |= PFLAG_PREPRESSED;
                        if (mPendingCheckForTap == null) {
                            mPendingCheckForTap = new CheckForTap();
                        }
                        mPendingCheckForTap.x = event.getX();
                        mPendingCheckForTap.y = event.getY();
                        postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
                    } else {
                        // Not inside a scrolling container, so show the feedback right away
                        setPressed(true, x, y);
                        checkForLongClick(0, x, y);
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                    setPressed(false);
                    removeTapCallback();
                    removeLongPressCallback();
                    mInContextButtonPress = false;
                    mHasPerformedLongPress = false;
                    mIgnoreNextUpEvent = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    drawableHotspotChanged(x, y);

                    // Be lenient about moving outside of buttons
                    if (!pointInView(x, y, mTouchSlop)) {
                        // Outside button
                        removeTapCallback();
                        if ((mPrivateFlags & PFLAG_PRESSED) != 0) {
                            // Remove any future long press/tap checks
                            removeLongPressCallback();

                            setPressed(false);
                        }
                    }
                    break;
            }

            return true;
        }

        return false;
    }





```
View的disPatchTouchEvent非常简单，就是判断了touchListener的返回值，为true，则把事件消费掉，整个响应结束。如果他的返回值为false的话则会调用onTouchEvent方法，经过一些Clickble之类的判断是否能进行点击之类的响应，如果能的话则进入performClick（也就是所有的点击，长按等等回调），然后把事件给消费掉整个响应结束。若以上两个方法都没有响应事件则diaPatchTouchEvent的值会false，会交给他的上一层ViewGroup去响应他的事件。由上ViewGroup的dispatchTouchEvent，但是这时候传递的参数是null,根据dispatchTransformedTouchEvent，如果view为null的话就直接调用父类的dispatchTouchEvent也就是onTouchEvent，然后重复此操作直到被响应（也就是dispatchTouchEvent返回值为true）。

```
 //如果没有触摸目标  
            if (mFirstTouchTarget == null) {  
                // No touch targets so treat this as an ordinary view.  
                //那么就表示我们要自己在这个ViewGroup处理这个触摸事件了  
                handled = dispatchTransformedTouchEvent(ev, canceled, null,  
                        TouchTarget.ALL_POINTER_IDS);  
            } 
                    
                    
```
整体的流程图如下：

![touch pic](https://github.com/litian1a/AndrodNote/blob/master/pic/touch.png)


