package com.github.kancyframework.springx.swing.utils;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.utils.CollectionUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * DropTargetUtils
 *
 * @author kancy
 * @date 2021/1/9 19:14
 */
public class DropTargetUtils {

    public static void addDropTarget(Component c, DropTargetListener listener){
        DropTarget dropTarget = new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, listener);
    }

    public static void addDropTarget(Component c, DropTargetAdapter listener){
        DropTarget dropTarget = new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, listener);
    }

    public static void addJavaFileDropTarget(Component c, Function<DropTargetDropEvent, Boolean> listener){
        DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        dtde.dropComplete(listener.apply(dtde));
                    } catch (Exception e) {
                        dtde.rejectDrop();
                    }
                } else {
                    dtde.rejectDrop();
                }
            }
        };
        DropTarget dropTarget = new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, dropTargetAdapter);
    }

    public static void addJavaFileDropTarget(Function<List<File>, Boolean> listener, Component ... components){
        if (Objects.isNull(components) || components.length == 0){
            return;
        }
        DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        List<File> list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!CollectionUtils.isEmpty(list)) {
                            dtde.dropComplete(listener.apply(list));
                        }
                    } catch (Exception e) {
                        dtde.rejectDrop();
                        Log.error("addJavaFileDropTarget error", e);
                    }
                } else {
                    dtde.rejectDrop();
                }
            }
        };
        Arrays.stream(components).forEach(component -> new DropTarget(component, DnDConstants.ACTION_COPY_OR_MOVE, dropTargetAdapter));
    }
}
