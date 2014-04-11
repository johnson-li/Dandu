package com.dandu.fdureader;

import java.util.ArrayList;

/**
 * Created by johnson on 4/10/14.
 */
public class Magazine extends Term{
    //数组，保存这个刊物下的文章
    public ArrayList<Post> posts = new ArrayList<Post>();

    public Magazine() {
    }

    public Magazine(Term t) {
        term_id = t.term_id;
        parent = t.parent;
        name = t.name;
        slug = t.slug;
        description = t.description;
        term_taxonomy_id = t.term_taxonomy_id;
        count = t.count;
        coverImage = t.coverImage;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    @Override
    public String toString() {
        return super.toString() + '\n';
    }
}
