package com.hk.im.flow.data.cdc.process;

import com.hk.im.domain.entity.Note;

/**
 * @author : HK意境
 * @ClassName : NoteProcessor
 * @date : 2023/5/15 19:05
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class NoteProcessor implements RecordProcessor<Note> {
    @Override
    public Note create(Note note) {
        return null;
    }

    @Override
    public Note update(Note before, Note after) {
        return null;
    }

    @Override
    public Note remove(Note before, Note after) {
        return null;
    }
}
