package community.service;

import community.mapper.UserMapper;
import community.model.UserExample;
import community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    UserMapper userMapper = null;

    public void insertOrUpdate(User userDB) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(userDB.getAccountId());
        List<community.model.User> users = userMapper.selectByExample(example);

        if(users.size() == 0){
//            插入
            userDB.setGmtCreate(System.currentTimeMillis());
            userDB.setGmtModify(System.currentTimeMillis());
            userMapper.insert(userDB);
        }else{
            userDB.setGmtModify(System.currentTimeMillis());
            UserExample userExample = new UserExample();
            example.createCriteria()
                    .andAccountIdEqualTo(userDB.getAccountId());
            userMapper.updateByExampleSelective(userDB, userExample);
//            userMapper.updateUserByAccountID(userDB);
        }
    }
}
