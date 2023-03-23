import { View, StyleSheet} from "react-native";
import AddHashtag from "../components/making/AddHashtag";
import { GlobalColors } from "../constants/color";

const MakingDetailScreen = () => {
  return (
    <View style={styles.container}>
      <View style={styles.inform}>
        <AddHashtag />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: GlobalColors.colors.primary500,
  },
  inform: {
    alignItems: "center",
    top: 100,
    width: 400,
    width: "100%",
    height: "90%",
    backgroundColor: GlobalColors.colors.white500,
    borderTopLeftRadius: 40,
    borderTopRightRadius: 40,
  },
});
export default MakingDetailScreen;
