package com.dandu.fdureader;

import java.util.ArrayList;

/**
 * Created by johnson on 4/10/14.
 */
public class Press extends Term
{
    //数组，保存这个媒体发行的所有刊物
    public ArrayList<Magazine> magazines = new ArrayList<Magazine>();
    public Press()
    {
    }
    public Press( Term t )
    {
        term_id = t.term_id;
        parent = t.parent;
        name = t.name;
        slug = t.slug;
        description = t.description;
        term_taxonomy_id = t.term_taxonomy_id;
        count = t.count;
        coverImage = t.coverImage;
    }
    public void addMagazine( Magazine m )
    {
        magazines.add( m );
    }
    public Magazine getMagazine( Integer id )
    {
        for( Magazine m : magazines )
        {
            if ( m.term_id == id )
            {
                return m;
            }
        }
        return null;
    }
    @Override
    public String toString()
    {
        return super.toString()+'\n'+
                magazines.toString()+'\n';

    }
}
