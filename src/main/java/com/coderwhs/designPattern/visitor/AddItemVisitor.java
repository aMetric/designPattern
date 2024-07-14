package com.coderwhs.designPattern.visitor;

import com.coderwhs.designPattern.composite.AbstractProductItem;
import com.coderwhs.designPattern.composite.ProductComposite;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import com.coderwhs.designPattern.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/6/22 10:42
 * @description: 具体访问者-支持商品类目的添加
 */
@Component
public class AddItemVisitor implements ItemVisitor<AbstractProductItem>{

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    public AbstractProductItem visitor(AbstractProductItem productItem) {
        ProductComposite currentItem = (ProductComposite)redisCommonProcessor.get(RedisKeyUtils.PRODUCT_ITEM_KEY);

        //需要新增的商品类目
        ProductComposite addItem = (ProductComposite) productItem;

        //如果新增节点的父节点是当前节点，则直接添加
        if(addItem.getPid() == currentItem.getId()){
            currentItem.addProductItem(addItem);
            return currentItem;
        }

        //通过addChild方法进行递归寻找新增类目的插入点
        addChild(addItem,currentItem);
        return currentItem;

    }

    /**
     * 递归寻找新增类目的插入点
     * @param addItem
     * @param currentItem
     */
    private void addChild(ProductComposite addItem, ProductComposite currentItem) {
        for(AbstractProductItem abstractProductItem : currentItem.getChild()){
            ProductComposite item = (ProductComposite) abstractProductItem;
            if(item.getId() == addItem.getPid()){
                item.addProductItem(addItem);
                break;
            }else{
                addChild(addItem,item);
            }
        }
    }
}
