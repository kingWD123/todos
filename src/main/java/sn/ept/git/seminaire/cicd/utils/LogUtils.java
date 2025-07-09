package sn.ept.git.seminaire.cicd.utils;

import lombok.experimental.UtilityClass;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;

import java.util.Optional;

/**
 * @author ISENE
 */
@UtilityClass
public final class LogUtils {
    public final String LOG_START="{} : starts method : {}";
}