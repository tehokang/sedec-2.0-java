package sedec2.arib.tlv.container.mmt.si;

import sedec2.arib.tlv.container.mmt.si.tables.ConditionalAccessTable;
import sedec2.arib.tlv.container.mmt.si.tables.DataAssetManagementTable;
import sedec2.arib.tlv.container.mmt.si.tables.DataContentConfigurationTable;
import sedec2.arib.tlv.container.mmt.si.tables.DataDirectoryManagementTable;
import sedec2.arib.tlv.container.mmt.si.tables.DownloadControlMessage;
import sedec2.arib.tlv.container.mmt.si.tables.DownloadManagementMessage;
import sedec2.arib.tlv.container.mmt.si.tables.EntitlementControlMessage;
import sedec2.arib.tlv.container.mmt.si.tables.EventMessageTable;
import sedec2.arib.tlv.container.mmt.si.tables.LayoutConfigurationTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_ApplicationInformationTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_BroadcasterInformationTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_CommonDataTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_EventInformationTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_ServiceDescriptionTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_SoftwareDownloadTriggerTable;
import sedec2.arib.tlv.container.mmt.si.tables.MH_TimeOffsetTable;
import sedec2.arib.tlv.container.mmt.si.tables.MMT_PackageTable;
import sedec2.arib.tlv.container.mmt.si.tables.PackageListTable;
import sedec2.arib.tlv.container.mmt.si.tables.UnknownTable;
import sedec2.base.Table;

/**
 * Factory to obtain a kind of table of MMT-SI like below.
 * <ul>
 * <li> {@link ConditionalAccessTable}
 * <li> {@link DataAssetManagementTable}
 * <li> {@link DataContentConfigurationTable}
 * <li> {@link DataDirectoryManagementTable}
 * <li> {@link DownloadControlMessage}
 * <li> {@link DownloadManagementMessage}
 * <li> {@link EntitlementControlMessage}
 * <li> {@link EventMessageTable}
 * <li> {@link LayoutConfigurationTable}
 * <li> {@link MH_ApplicationInformationTable}
 * <li> {@link MH_BroadcasterInformationTable}
 * <li> {@link MH_CommonDataTable}
 * <li> {@link MH_EventInformationTable}
 * <li> {@link MH_ServiceDescriptionTable}
 * <li> {@link MH_SoftwareDownloadTriggerTable}
 * <li> {@link MH_TimeOffsetTable}
 * <li> {@link MMT_PackageTable}
 * <li> {@link PackageListTable}
 * </ul>
 */
public class TableFactory {
    /** MMT-PT */
    public final static byte MMT_PACKAGE_TABLE = (byte) 0x20;
    public final static byte MPT = MMT_PACKAGE_TABLE;

    /** PLT */
    public final static byte PACKAGE_LIST_TABLE = (byte) 0x80;
    public final static byte PLT = PACKAGE_LIST_TABLE;

    /** LCT */
    public final static byte LAYOUT_CONFIGURATION_TABLE = (byte) 0x81;
    public final static byte LCT = LAYOUT_CONFIGURATION_TABLE;

    /** ECM */
    public final static byte ENTITLEMENT_CONTROL_MESSAGE_1 = (byte) 0x82;
    public final static byte ECM_1 = ENTITLEMENT_CONTROL_MESSAGE_1;

    public final static byte ENTITLEMENT_CONTROL_MESSAGE_2 = (byte) 0x83;
    public final static byte ECM_2 = ENTITLEMENT_CONTROL_MESSAGE_2;

    /** EMM */
    public final static byte ENTITLEMENT_MANAGEMENT_MESSAGE_1 = (byte) 0x84;
    public final static byte EMM_1 = ENTITLEMENT_MANAGEMENT_MESSAGE_1;

    public final static byte ENTITLEMENT_MANAGEMENT_MESSAGE_2 = (byte) 0x85;
    public final static byte EMM_2 = ENTITLEMENT_MANAGEMENT_MESSAGE_2;

    /** CAT */
    public final static byte CONDITIONAL_ACCESS_TABLE = (byte) 0x86;
    public final static byte CAT = CONDITIONAL_ACCESS_TABLE;

    /** DCM */
    public final static byte DOWNLOAD_CONTROL_MESSAGE_1 = (byte) 0x87;
    public final static byte DCM_1 = DOWNLOAD_CONTROL_MESSAGE_1;

    public final static byte DOWNLOAD_CONTROL_MESSAGE_2 = (byte) 0x88;
    public final static byte DCM_2 = DOWNLOAD_CONTROL_MESSAGE_2;

    /** DMM */
    public final static byte DOWNLOAD_MANAGEMENT_MESSAGE_1 = (byte) 0x89;
    public final static byte DMM_1 = DOWNLOAD_MANAGEMENT_MESSAGE_1;

    public final static byte DOWNLOAD_MANAGEMENT_MESSAGE_2 = (byte) 0x8a;
    public final static byte DMM_2 = DOWNLOAD_MANAGEMENT_MESSAGE_2;

    /** MH-EIT 0x8b, 0x8c~0x9b */
    public final static byte ACTUAL_MH_EVENT_INFORMATION_TABLE = (byte) 0x8b;
    public final static byte MH_EIT_ACTUAL = ACTUAL_MH_EVENT_INFORMATION_TABLE;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_1 = (byte) 0x8c;
    public final static byte MH_EIT_AUTO_1 = AUTO_MH_EVENT_INFORMATION_TABLE_1;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_2 = (byte) 0x8d;
    public final static byte MH_EIT_AUTO_2 = AUTO_MH_EVENT_INFORMATION_TABLE_2;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_3 = (byte) 0x8e;
    public final static byte MH_EIT_AUTO_3 = AUTO_MH_EVENT_INFORMATION_TABLE_3;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_4 = (byte) 0x8f;
    public final static byte MH_EIT_AUTO_4 = AUTO_MH_EVENT_INFORMATION_TABLE_4;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_5 = (byte) 0x90;
    public final static byte MH_EIT_AUTO_5 = AUTO_MH_EVENT_INFORMATION_TABLE_5;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_6 = (byte) 0x91;
    public final static byte MH_EIT_AUTO_6 = AUTO_MH_EVENT_INFORMATION_TABLE_6;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_7 = (byte) 0x92;
    public final static byte MH_EIT_AUTO_7 = AUTO_MH_EVENT_INFORMATION_TABLE_7;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_8 = (byte) 0x93;
    public final static byte MH_EIT_AUTO_8 = AUTO_MH_EVENT_INFORMATION_TABLE_8;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_9 = (byte) 0x94;
    public final static byte MH_EIT_AUTO_9 = AUTO_MH_EVENT_INFORMATION_TABLE_9;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_10 = (byte) 0x95;
    public final static byte MH_EIT_AUTO_10 = AUTO_MH_EVENT_INFORMATION_TABLE_10;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_11 = (byte) 0x96;
    public final static byte MH_EIT_AUTO_11 = AUTO_MH_EVENT_INFORMATION_TABLE_11;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_12 = (byte) 0x97;
    public final static byte MH_EIT_AUTO_12 = AUTO_MH_EVENT_INFORMATION_TABLE_12;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_13 = (byte) 0x98;
    public final static byte MH_EIT_AUTO_13 = AUTO_MH_EVENT_INFORMATION_TABLE_13;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_14 = (byte) 0x99;
    public final static byte MH_EIT_AUTO_14 = AUTO_MH_EVENT_INFORMATION_TABLE_14;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_15 = (byte) 0x9a;
    public final static byte MH_EIT_AUTO_15 = AUTO_MH_EVENT_INFORMATION_TABLE_15;

    public final static byte AUTO_MH_EVENT_INFORMATION_TABLE_16 = (byte) 0x9b;
    public final static byte MH_EIT_AUTO_16 = AUTO_MH_EVENT_INFORMATION_TABLE_16;

    /** MH-AIT */
    public final static byte MH_APPLICATION_INFORMATION_TABLE = (byte) 0x9c;
    public final static byte MH_AIT = MH_APPLICATION_INFORMATION_TABLE;

    /** MH-BIT */
    public final static byte MH_BROADCASTER_INFORMATION_TABLE = (byte) 0x9d;
    public final static byte MH_BIT = MH_BROADCASTER_INFORMATION_TABLE;

    /** MH-SDTT */
    public final static byte MH_SOFTWARE_DOWNLOAD_TRIGGER_TABLE = (byte) 0x9e;
    public final static byte MH_SDTT = MH_SOFTWARE_DOWNLOAD_TRIGGER_TABLE;

    /** MH-SDT */
    public final static byte ACTUAL_MH_SERVICE_DESCRIPTION_TABLE = (byte) 0x9f;
    public final static byte MH_SDT_ACTUAL = ACTUAL_MH_SERVICE_DESCRIPTION_TABLE;

    public final static byte OTHER_MH_SERVICE_DESCRIPTION_TABLE = (byte) 0xa0;
    public final static byte MH_SDT_OTHER = OTHER_MH_SERVICE_DESCRIPTION_TABLE;

    /** MH-TOT */
    public final static byte MH_TIME_OFFSET_TABLE = (byte) 0xa1;
    public final static byte MH_TOT = MH_TIME_OFFSET_TABLE;

    /** MH-CDT */
    public final static byte MH_COMMON_DATA_TABLE = (byte) 0xa2;
    public final static byte MH_CDT = MH_COMMON_DATA_TABLE;

    /** DDMT */
    public final static byte DATA_DIRECTORY_MANAGEMENT_TABLE = (byte) 0xa3;
    public final static byte DDMT = DATA_DIRECTORY_MANAGEMENT_TABLE;

    /** DAMT */
    public final static byte DATA_ASSET_MANAGEMENT_TABLE = (byte) 0xa4;
    public final static byte DAMT = DATA_ASSET_MANAGEMENT_TABLE;

    /** DCCT */
    public final static byte DATA_CONTENT_MANAGEMENT_TABLE = (byte) 0xa5;
    public final static byte DCMT = DATA_CONTENT_MANAGEMENT_TABLE;

    /** EMT */
    public final static byte EVENT_MESSAGE_TABLE = (byte) 0xa6;
    public final static byte EMT = EVENT_MESSAGE_TABLE;

    public final static byte UNKNOWN_TABLE = (byte) 0xff;

    public static Table createTable(byte[] buffer) {
        byte table_id = (byte)(buffer[0] & 0xff);

        switch ( table_id ) {
            case DATA_CONTENT_MANAGEMENT_TABLE:
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
            case MH_SOFTWARE_DOWNLOAD_TRIGGER_TABLE:
                return new MH_SoftwareDownloadTriggerTable(buffer);
            case MH_BROADCASTER_INFORMATION_TABLE:
                return new MH_BroadcasterInformationTable(buffer);
            case PACKAGE_LIST_TABLE:
                return new PackageListTable(buffer);
            case ACTUAL_MH_SERVICE_DESCRIPTION_TABLE:
            case OTHER_MH_SERVICE_DESCRIPTION_TABLE:
                return new MH_ServiceDescriptionTable(buffer);
            case MH_APPLICATION_INFORMATION_TABLE:
                return new MH_ApplicationInformationTable(buffer);
            case MH_COMMON_DATA_TABLE:
                return new MH_CommonDataTable(buffer);
            case MH_TIME_OFFSET_TABLE:
                return new MH_TimeOffsetTable(buffer);
            case LAYOUT_CONFIGURATION_TABLE:
                return new LayoutConfigurationTable(buffer);
            case ACTUAL_MH_EVENT_INFORMATION_TABLE:
            case AUTO_MH_EVENT_INFORMATION_TABLE_1:
            case AUTO_MH_EVENT_INFORMATION_TABLE_2:
            case AUTO_MH_EVENT_INFORMATION_TABLE_3:
            case AUTO_MH_EVENT_INFORMATION_TABLE_4:
            case AUTO_MH_EVENT_INFORMATION_TABLE_5:
            case AUTO_MH_EVENT_INFORMATION_TABLE_6:
            case AUTO_MH_EVENT_INFORMATION_TABLE_7:
            case AUTO_MH_EVENT_INFORMATION_TABLE_8:
            case AUTO_MH_EVENT_INFORMATION_TABLE_9:
            case AUTO_MH_EVENT_INFORMATION_TABLE_10:
            case AUTO_MH_EVENT_INFORMATION_TABLE_11:
            case AUTO_MH_EVENT_INFORMATION_TABLE_12:
            case AUTO_MH_EVENT_INFORMATION_TABLE_13:
            case AUTO_MH_EVENT_INFORMATION_TABLE_14:
            case AUTO_MH_EVENT_INFORMATION_TABLE_15:
            case AUTO_MH_EVENT_INFORMATION_TABLE_16:
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
