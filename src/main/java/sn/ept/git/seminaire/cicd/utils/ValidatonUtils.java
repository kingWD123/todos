package sn.ept.git.seminaire.cicd.utils;

import lombok.experimental.UtilityClass;
import sn.ept.git.seminaire.cicd.exceptions.InvalidException;

/**
 * @author ISENE
 */

@UtilityClass
public final class ValidatonUtils {

    public static  void validatePageMeta(int page,int size){
        if(page< 0){
            throw new InvalidException("Page index must not be less than zero");
        }
        if(size< 1){
            throw new InvalidException("Page size must not be less than one");
        }

        if(size> 20){
            throw new InvalidException("Page size must not be too large");
        }

    }
}