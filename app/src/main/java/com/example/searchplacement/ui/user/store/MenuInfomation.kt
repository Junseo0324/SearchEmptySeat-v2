
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel


@Composable
fun MenuDisplayScreen(
    storePk: Long,
    menuViewModel: MenuViewModel,
    menuSectionViewModel: MenuSectionViewModel,
    token: String
) {
    val imageLoader = rememberImageLoaderWithToken()
    val menus = menuViewModel.menus.collectAsState().value
    val sections = menuSectionViewModel.sections.collectAsState().value

    // 데이터 불러오기
    LaunchedEffect(storePk) {
        menuSectionViewModel.fetchSections(storePk)
        menuViewModel.fetchMenus(storePk)
    }

    val sortedSections = sections.sortedBy { it.priority }
    val sectionedMenus = sortedSections.associateWith { section ->
        menus.filter { it.section?.sectionPK == section.sectionPK }
    }
    val noSectionMenus = menus.filter { it.section == null }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(Dimens.Medium).heightIn(max = 400.dp)) {
        sectionedMenus.forEach { (section, menusInSection) ->
            item {
                Text(
                    text = section.name,
                    style = AppTextStyle.Body,
                    modifier = Modifier.padding(vertical = Dimens.Small)
                )
            }
            items(menusInSection) { menu ->
                MenuDisplayItem(menu = menu, imageLoader = imageLoader)
            }
        }

        if (noSectionMenus.isNotEmpty()) {
            item {
                Text(
                    text = "기타",
                    style = AppTextStyle.BodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(noSectionMenus) { menu ->
                MenuDisplayItem(menu = menu, imageLoader = imageLoader)
            }
        }
    }
}


@Composable
fun MenuDisplayItem(menu: MenuResponse, imageLoader: ImageLoader) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = menu.image?.firstOrNull()
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = "${AppModule.BASE_URL}/api/files/$imageUrl",
                imageLoader = imageLoader,
                contentDescription = menu.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            Box(
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("No Img", style = AppTextStyle.BodySmall)
            }
        }

        Spacer(Modifier.width(Dimens.Medium))

        Column {
            Text(menu.name, style = AppTextStyle.BodyLarge.copy(fontSize = 18.sp ))
            Text("${menu.price}원", style = AppTextStyle.Body.copy(color = Color.DarkGray))
            if (menu.description.isNotBlank()) {
                Text(menu.description, style = AppTextStyle.Caption, maxLines = 2)
            }
        }
    }
}


