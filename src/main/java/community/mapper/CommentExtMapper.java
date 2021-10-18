package community.mapper;

import community.model.Comment;
import community.model.CommentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentExtMapper {
    int increaseComment(Comment record);
}