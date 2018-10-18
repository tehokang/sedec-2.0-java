package sedec2.arib.mmt.si;

import sedec2.arib.mmt.si.tables.ConditionalAccessTable;
import sedec2.arib.mmt.si.tables.DataAssetManagementTable;
import sedec2.arib.mmt.si.tables.DataContentConfigurationTable;
import sedec2.arib.mmt.si.tables.DataDirectoryManagementTable;
import sedec2.arib.mmt.si.tables.DownloadControlMessage;
import sedec2.arib.mmt.si.tables.DownloadManagementMessage;
import sedec2.arib.mmt.si.tables.EntitlementControlMessage;
import sedec2.arib.mmt.si.tables.EventMessageTable;
import sedec2.arib.mmt.si.tables.LayoutConfigurationTable;
import sedec2.arib.mmt.si.tables.MH_ApplicationInformationTable;
import sedec2.arib.mmt.si.tables.MH_BroadcasterInformationTable;
import sedec2.arib.mmt.si.tables.MH_CommonDataTable;
import sedec2.arib.mmt.si.tables.MH_EventInformationTable;
import sedec2.arib.mmt.si.tables.MH_ServiceDescriptionTable;
import sedec2.arib.mmt.si.tables.MH_SoftwareDownloadTriggerTable;
import sedec2.arib.mmt.si.tables.MH_TimeOffsetTable;
import sedec2.arib.mmt.si.tables.MMT_PackageTable;
import sedec2.arib.mmt.si.tables.PackageListTable;
import sedec2.base.Table;
import sedec2.base.UnknownTable;

public class TableFactory {
    /** MMT-PT */
    public final static int MMT_PACKAGE_TABLE = 0x20;
    
    /** PLT */
    public final static int PACKAGE_LIST_TABLE = 0x80;
    
    /** ECM */
    public final static int ENTITLEMENT_CONTROL_MESSAGE_1 = 0x82;
    public final static int ENTITLEMENT_CONTROL_MESSAGE_2 = 0x83;
    
    /** EMM */
    public final static int ENTITLEMENT_MANAGEMENT_MESSAGE_1 = 0x84;
    public final static int ENTITLEMENT_MANAGEMENT_MESSAGE_2 = 0x85;
    
    /** DCM */
    public final static int DOWNLOAD_CONTROL_MESSAGE_1 = 0x87;
    public final static int DOWNLOAD_CONTROL_MESSAGE_2 = 0x88;
    
    /** DMM */
    public final static int DOWNLOAD_MANAGEMENT_MESSAGE_1 = 0x89;
    public final static int DOWNLOAD_MANAGEMENT_MESSAGE_2 = 0x8a;
    
    /** CAT */
    public final static int CONDITIONAL_ACCESS_TABLE = 0x86;
    
    /** MH-SDT */
    public final static int ACTUAL_MPEGH_SERVICE_DESCRIPTION_TABLE = 0x9f;
    public final static int OTHER_MPEGH_SERVICE_DESCRIPTION_TABLE = 0xa0;
    
    /** MH-AIT */
    public final static int MPEGH_APPLICATION_INFORMATION_TABLE = 0x9c;
    
    /** MH-BIT */
    public final static int MPEGH_BROADCASTER_INFORMATION_TABLE = 0x9d;
    
    /** MH-SDTT */
    public final static int MPEGH_SOFTWARE_DOWNLOAD_TRIGGER_TABLE = 0x9e;
    
    /** MH-CDT */
    public final static int MPEGH_COMMON_DATA_TABLE = 0xa2;
    
    /** MH-TOT */
    public final static int MPEGH_TIME_OFFSET_TABLE = 0xa1;
    
    /** DDMT */
    public final static int DATA_DIRECTORY_MANAGEMENT_TABLE = 0xa3;
    
    /** DAMT */
    public final static int DATA_ASSET_MANAGEMENT_TABLE = 0xa4;
    
    /** DCCT */
    public final static int DATA_CONTENT_CONFIGURATION_TABLE = 0xa5;
    
    /** EMT */
    public final static int EVENT_MESSAGE_TABLE = 0xa6;
    
    /** LCT */
    public final static int LAYOUT_CONFIGURATION_TABLE = 0x81;
    
    /** MH-EIT 0x8b, 0x8c~0x9b */
    public final static int ACTUAL_MPEGH_EVENT_INFORMATION_TABLE = 0x8b;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_1 = 0x8c;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_2 = 0x8d;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_3 = 0x8e;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_4 = 0x8f;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_5 = 0x90;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_6 = 0x91;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_7 = 0x92;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_8 = 0x93;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_9 = 0x94;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_10 = 0x95;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_11 = 0x96;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_12 = 0x97;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_13 = 0x98;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_14 = 0x99;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_15 = 0x9a;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_16 = 0x9b;
    
    public final static int UNKNOWN_TABLE = 0xff;

    public static Table CreateTable(byte[] buffer) {
        int table_id = (buffer[0] & 0xff);
        
        switch(table_id) {
            case DATA_CONTENT_CONFIGURATION_TABLE:
                return new DataContentConfigurationTable(buffer);
            case DATA_ASSET_MANAGEMENT_TABLE:
                return new DataAssetManagementTable(buffer);
            case DATA_DIRECTORY_MANAGEMENT_TABLE:
                return new DataDirectoryManagementTable(buffer);
            case DOWNLOAD_MANAGEMENT_MESSAGE_1:
            case DOWNLOAD_MANAGEMENT_MESSAGE_2:
                return new DownloadManagementMessage(buffer);
            case DOWNLOAD_CONTROL_MESSAGE_1:
            case DOWNLOAD_CONTROL_MESSAGE_2:
                return new DownloadControlMessage(buffer);
            case ENTITLEMENT_CONTROL_MESSAGE_1:
            case ENTITLEMENT_CONTROL_MESSAGE_2:
                return new EntitlementControlMessage(buffer);
            case MMT_PACKAGE_TABLE:
                return new MMT_PackageTable(buffer);
            case EVENT_MESSAGE_TABLE:
                return new EventMessageTable(buffer);
            case CONDITIONAL_ACCESS_TABLE:
                return new ConditionalAccessTable(buffer);
            case MPEGH_SOFTWARE_DOWNLOAD_TRIGGER_TABLE:
                return new MH_SoftwareDownloadTriggerTable(buffer);
            case MPEGH_BROADCASTER_INFORMATION_TABLE:
                return new MH_BroadcasterInformationTable(buffer);
            case PACKAGE_LIST_TABLE:
                return new PackageListTable(buffer);
            case ACTUAL_MPEGH_SERVICE_DESCRIPTION_TABLE:
            case OTHER_MPEGH_SERVICE_DESCRIPTION_TABLE:
                return new MH_ServiceDescriptionTable(buffer);
            case MPEGH_APPLICATION_INFORMATION_TABLE:
                return new MH_ApplicationInformationTable(buffer);
            case MPEGH_COMMON_DATA_TABLE:
                return new MH_CommonDataTable(buffer);
            case MPEGH_TIME_OFFSET_TABLE:
                return new MH_TimeOffsetTable(buffer);
            case LAYOUT_CONFIGURATION_TABLE:
                return new LayoutConfigurationTable(buffer);
            case ACTUAL_MPEGH_EVENT_INFORMATION_TABLE:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_1:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_2:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_3:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_4:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_5:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_6:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_7:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_8:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_9:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_10:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_11:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_12:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_13:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_14:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_15:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_16:
                return new MH_EventInformationTable(buffer);
            default:
                return new UnknownTable(buffer);
        }
    }
    
    private TableFactory() {
        /**
         * @warning Nothing to do since this factory isn't working as instance
         */
    }
}
