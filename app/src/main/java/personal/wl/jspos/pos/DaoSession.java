package personal.wl.jspos.pos;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.ProductBarCode;
import personal.wl.jspos.pos.ProductBranchRel;
import personal.wl.jspos.pos.ProductBarCodeBranchRel;
import personal.wl.jspos.pos.ProductClass;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;
import personal.wl.jspos.pos.Branch;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.PosMachine;
import personal.wl.jspos.pos.MobileDevice;

import personal.wl.jspos.pos.ProductDao;
import personal.wl.jspos.pos.ProductBarCodeDao;
import personal.wl.jspos.pos.ProductBranchRelDao;
import personal.wl.jspos.pos.ProductBarCodeBranchRelDao;
import personal.wl.jspos.pos.ProductClassDao;
import personal.wl.jspos.pos.SaleDailyDao;
import personal.wl.jspos.pos.SalePayModeDao;
import personal.wl.jspos.pos.BranchDao;
import personal.wl.jspos.pos.BranchEmployeeDao;
import personal.wl.jspos.pos.PosMachineDao;
import personal.wl.jspos.pos.MobileDeviceDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig productDaoConfig;
    private final DaoConfig productBarCodeDaoConfig;
    private final DaoConfig productBranchRelDaoConfig;
    private final DaoConfig productBarCodeBranchRelDaoConfig;
    private final DaoConfig productClassDaoConfig;
    private final DaoConfig saleDailyDaoConfig;
    private final DaoConfig salePayModeDaoConfig;
    private final DaoConfig branchDaoConfig;
    private final DaoConfig branchEmployeeDaoConfig;
    private final DaoConfig posMachineDaoConfig;
    private final DaoConfig mobileDeviceDaoConfig;

    private final ProductDao productDao;
    private final ProductBarCodeDao productBarCodeDao;
    private final ProductBranchRelDao productBranchRelDao;
    private final ProductBarCodeBranchRelDao productBarCodeBranchRelDao;
    private final ProductClassDao productClassDao;
    private final SaleDailyDao saleDailyDao;
    private final SalePayModeDao salePayModeDao;
    private final BranchDao branchDao;
    private final BranchEmployeeDao branchEmployeeDao;
    private final PosMachineDao posMachineDao;
    private final MobileDeviceDao mobileDeviceDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        productDaoConfig = daoConfigMap.get(ProductDao.class).clone();
        productDaoConfig.initIdentityScope(type);

        productBarCodeDaoConfig = daoConfigMap.get(ProductBarCodeDao.class).clone();
        productBarCodeDaoConfig.initIdentityScope(type);

        productBranchRelDaoConfig = daoConfigMap.get(ProductBranchRelDao.class).clone();
        productBranchRelDaoConfig.initIdentityScope(type);

        productBarCodeBranchRelDaoConfig = daoConfigMap.get(ProductBarCodeBranchRelDao.class).clone();
        productBarCodeBranchRelDaoConfig.initIdentityScope(type);

        productClassDaoConfig = daoConfigMap.get(ProductClassDao.class).clone();
        productClassDaoConfig.initIdentityScope(type);

        saleDailyDaoConfig = daoConfigMap.get(SaleDailyDao.class).clone();
        saleDailyDaoConfig.initIdentityScope(type);

        salePayModeDaoConfig = daoConfigMap.get(SalePayModeDao.class).clone();
        salePayModeDaoConfig.initIdentityScope(type);

        branchDaoConfig = daoConfigMap.get(BranchDao.class).clone();
        branchDaoConfig.initIdentityScope(type);

        branchEmployeeDaoConfig = daoConfigMap.get(BranchEmployeeDao.class).clone();
        branchEmployeeDaoConfig.initIdentityScope(type);

        posMachineDaoConfig = daoConfigMap.get(PosMachineDao.class).clone();
        posMachineDaoConfig.initIdentityScope(type);

        mobileDeviceDaoConfig = daoConfigMap.get(MobileDeviceDao.class).clone();
        mobileDeviceDaoConfig.initIdentityScope(type);

        productDao = new ProductDao(productDaoConfig, this);
        productBarCodeDao = new ProductBarCodeDao(productBarCodeDaoConfig, this);
        productBranchRelDao = new ProductBranchRelDao(productBranchRelDaoConfig, this);
        productBarCodeBranchRelDao = new ProductBarCodeBranchRelDao(productBarCodeBranchRelDaoConfig, this);
        productClassDao = new ProductClassDao(productClassDaoConfig, this);
        saleDailyDao = new SaleDailyDao(saleDailyDaoConfig, this);
        salePayModeDao = new SalePayModeDao(salePayModeDaoConfig, this);
        branchDao = new BranchDao(branchDaoConfig, this);
        branchEmployeeDao = new BranchEmployeeDao(branchEmployeeDaoConfig, this);
        posMachineDao = new PosMachineDao(posMachineDaoConfig, this);
        mobileDeviceDao = new MobileDeviceDao(mobileDeviceDaoConfig, this);

        registerDao(Product.class, productDao);
        registerDao(ProductBarCode.class, productBarCodeDao);
        registerDao(ProductBranchRel.class, productBranchRelDao);
        registerDao(ProductBarCodeBranchRel.class, productBarCodeBranchRelDao);
        registerDao(ProductClass.class, productClassDao);
        registerDao(SaleDaily.class, saleDailyDao);
        registerDao(SalePayMode.class, salePayModeDao);
        registerDao(Branch.class, branchDao);
        registerDao(BranchEmployee.class, branchEmployeeDao);
        registerDao(PosMachine.class, posMachineDao);
        registerDao(MobileDevice.class, mobileDeviceDao);
    }
    
    public void clear() {
        productDaoConfig.clearIdentityScope();
        productBarCodeDaoConfig.clearIdentityScope();
        productBranchRelDaoConfig.clearIdentityScope();
        productBarCodeBranchRelDaoConfig.clearIdentityScope();
        productClassDaoConfig.clearIdentityScope();
        saleDailyDaoConfig.clearIdentityScope();
        salePayModeDaoConfig.clearIdentityScope();
        branchDaoConfig.clearIdentityScope();
        branchEmployeeDaoConfig.clearIdentityScope();
        posMachineDaoConfig.clearIdentityScope();
        mobileDeviceDaoConfig.clearIdentityScope();
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public ProductBarCodeDao getProductBarCodeDao() {
        return productBarCodeDao;
    }

    public ProductBranchRelDao getProductBranchRelDao() {
        return productBranchRelDao;
    }

    public ProductBarCodeBranchRelDao getProductBarCodeBranchRelDao() {
        return productBarCodeBranchRelDao;
    }

    public ProductClassDao getProductClassDao() {
        return productClassDao;
    }

    public SaleDailyDao getSaleDailyDao() {
        return saleDailyDao;
    }

    public SalePayModeDao getSalePayModeDao() {
        return salePayModeDao;
    }

    public BranchDao getBranchDao() {
        return branchDao;
    }

    public BranchEmployeeDao getBranchEmployeeDao() {
        return branchEmployeeDao;
    }

    public PosMachineDao getPosMachineDao() {
        return posMachineDao;
    }

    public MobileDeviceDao getMobileDeviceDao() {
        return mobileDeviceDao;
    }

}
