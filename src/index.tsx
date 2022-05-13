import { requireNativeComponent, ViewStyle } from 'react-native';

type LongImageProps = {
  color: string;
  style: ViewStyle;
};

export const LongImageViewManager = requireNativeComponent<LongImageProps>(
'LongImageView'
);

export default LongImageViewManager;
