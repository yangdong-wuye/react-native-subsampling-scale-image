import {
  ImageSourcePropType,
  Platform,
  requireNativeComponent,
  StyleProp,
  ViewStyle,
} from 'react-native';
const LongImageViewManager =
  Platform.OS === 'android'
    ? requireNativeComponent<LongImageViewProps>('LongImageView')
    : null;

export default LongImageViewManager;

export enum LongImageViewMinimumScale {
  SCALE_TYPE_CENTER_INSIDE = 1,
  SCALE_TYPE_CENTER_CROP = 2,
  SCALE_TYPE_CUSTOM = 3,
  SCALE_TYPE_START = 4,
}

export interface LongImageViewProps {
  style?: StyleProp<ViewStyle>;
  source: ImageSourcePropType;
  quickScaleEnabled?: boolean;
  zoomEnabled?: boolean;
  panEnabled?: boolean;
  minimumScale?: LongImageViewMinimumScale;
  children?: React.ReactNode | null;
}
