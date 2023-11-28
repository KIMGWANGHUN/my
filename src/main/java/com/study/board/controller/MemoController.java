package com.study.board.controller;

import com.study.board.entity.Memo;
import com.study.board.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MemoController {
    @Autowired
    private MemoService memoService;

    @GetMapping("/memo/write") //localhost/8080/board/write
    public String boardWriteForm(){
        return "memowrite";
    }

    @PostMapping("/memo/memoEnter")
    public String boardWritePro(Memo memo, Model model, MultipartFile file) throws Exception {
        memoService.write(memo, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/memo/list");
        return "message";
    }

    @GetMapping("/memo/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Memo> list = memoService.memoList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "memolist";
    }

    @GetMapping("/memo/view")  // localhost:8080/board/view?id=1
    public String memoView(Model model, Integer id) {
        model.addAttribute("memo", memoService.memoView(id));
        return "memoview";
    }

    @GetMapping("/memo/delete")
    public String memoDelete(Integer id){
        memoService.memoDelete(id);
        return "redirect:/memo/list";
    }

    @GetMapping("/memo/modify/{id}")
    public String memoModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("memo", memoService.memoView(id));
        return "memomodify";
    }

    @PostMapping("/memo/update/{id}")
    public String memoUpdate(@PathVariable("id") Integer id, Memo memo, MultipartFile file) throws Exception {
        Memo memoTemp = memoService.memoView(id);
        memoTemp.setTitle(memo.getTitle());
        memoTemp.setContent(memo.getContent());
        memoService.write(memoTemp, file);
        return "redirect:/memo/list";
    }

}
