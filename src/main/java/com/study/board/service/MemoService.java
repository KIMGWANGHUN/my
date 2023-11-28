package com.study.board.service;

import com.study.board.entity.Memo;
import com.study.board.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    //글 작성
    public void write(Memo memo, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        memo.setFilename(fileName);
        memo.setFilepath("/files/"+fileName);

        memoRepository.save(memo);
    }

    //게시글 리스트 처리
    public Page<Memo> memoList(Pageable pageable) {
        return memoRepository.findAll(pageable);
    }

    //특정 게시글 불러오기
    public Memo memoView(Integer id){
        return memoRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void memoDelete(Integer id) {
        memoRepository.deleteById(id);
    }
}
