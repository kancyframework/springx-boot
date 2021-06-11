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
import java.util.function.Consumer;
/**
 * DropTargetUtils
 *
 * @author kancy
 * @date 2021/1/9 19:14
 */
public class DropTargetUtils {

    public void addDropTarget(Component c, DropTargetListener listener){
        DropTarget dropTarget = new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, listener);
    }

    public void addDropTarget(Component c, DropTargetAdapter listener){
        DropTarget dropTarget = new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, listener);
    }

    public void addJavaFileDropTarget(Component c, Consumer<DropTargetDropEvent> listener){
        DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    listener.accept(dtde);
                } else {
                    dtde.rejectDrop();
                }
            }
        };
        DropTarget dropTarget = new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, dropTargetAdapter);
    }

    public void addJavaFileDropTarget(Consumer<List<File>> listener, Component ... components){
        if (Objects.isNull(components) || components.length == 0){
            return;
        }
        DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        List<File> list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!CollectionUtils.isEmpty(list)) {
                            listener.accept(list);
                        }
                    } catch (Exception e) {
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
