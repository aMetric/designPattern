package com.coderwhs.designPattern.visitor;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.composite.AbstractProductItem;
import com.coderwhs.designPattern.composite.ProductComposite;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import com.coderwhs.designPattern.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/6/22 10:42
 * @description: 具体访问者-支持商品类目的删除
 */
@Component
public class DelItemVisitor implements ItemVisitor<AbstractProductItem>{

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    public AbstractProductItem visitor(AbstractProductItem productItem) {
        ProductComposite currentItem = (ProductComposite)redisCommonProcessor.get(RedisKeyUtils.PRODUCT_ITEM_KEY);

        //需要新增的商品类目
        ProductComposite delItem = (ProductComposite) productItem;

        //不可删除根节点
        ThrowUtils.throwIf(delItem.getId() == currentItem.getId(), ErrorCode.FORBIDDEN_ERROR,"根节点不能删除！");

        //如果被删除的节点是根节点，则直接删除
        if(delItem.getPid() == currentItem.getId()){
            currentItem.delProductChild(delItem);
            return currentItem;
        }

        //通过delChild方法进行递归寻找被删除的类目位置
        delChild(delItem,currentItem);

        return currentItem;

    }

    private void delChild(ProductComposite delItem, ProductComposite currentItem) {
        for(AbstractProductItem abstractProductItem : currentItem.getChild()){
            ProductComposite item = (ProductComposite) abstractProductItem;
            if(item.getId() == delItem.getPid()){
                item.delProductChild(delItem);
                break;
            }else{
                delChild(delItem,item);
            }
        }
    }
    }
