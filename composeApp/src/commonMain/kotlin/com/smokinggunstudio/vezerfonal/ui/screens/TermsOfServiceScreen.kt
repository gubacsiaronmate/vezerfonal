package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.terms_of_service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(onBack: Function) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.terms_of_service)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            item {
                Spacer(Modifier.height(Spacing.sm))
                Text(
                    text = "Vezérfonal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Effective date: 2026-03-20",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "Operated by: Gubacsi Áron Máté and Balogh Márk",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(Spacing.md))
                HorizontalDivider()
            }

            item {
                TosSection("1. Acceptance of Terms")
                TosBody(
                    "By registering for, accessing, or using the Vezérfonal application and associated services (collectively, the \"Service\"), you agree to be legally bound by these Terms of Service (\"Terms\"). If you are accessing the Service on behalf of an organisation, you represent that you have the authority to bind that organisation to these Terms, and references to \"you\" include that organisation.\n\nIf you do not agree to these Terms, you must not use the Service."
                )
            }

            item {
                TosSection("2. Description of the Service")
                TosBody(
                    "Vezérfonal is an organisational communication platform that enables authorised administrators to send targeted messages to individuals, groups, and defined recipients within a registered organisation. Recipients may respond to messages using emoji reactions. The Service is intended for use by schools, government agencies, commercial companies, and other organisations, as well as individual users who have registered in accordance with these Terms."
                )
            }

            item {
                TosSection("3. Beta Version")
                TosBody(
                    "The Service is currently offered as a beta version, free of charge. By using the beta version, you acknowledge that:\n\n• The Service may contain bugs, errors, or unfinished features.\n• Features, interfaces, and functionality may change significantly before general release.\n• Vezérfonal provides the beta Service as-is with no guarantees of uptime, data preservation, or feature continuity.\n• Vezérfonal reserves the right to terminate the beta version at any time with or without prior notice."
                )
            }

            item {
                TosSection("4. Paid Service")
                TosBody(
                    "Upon general release, the Service may transition to a paid model. Organisations wishing to continue using Vezérfonal after the beta period ends will be required to enter into a separate written contract with Vezérfonal, which will include pricing, payment terms, and any service-level commitments. Individual users and organisations not covered by such a contract will be notified of pricing terms prior to any charge being applied.\n\nNo retrospective fees will be charged for usage during the beta period."
                )
            }

            item {
                TosSection("5. Eligibility")
                TosBody(
                    "The Service is available to any person or organisation capable of entering into a legally binding contract under applicable law. There is no minimum age requirement imposed by Vezérfonal on the basis of content, as Vezérfonal does not itself produce or publish any age-restricted content. However, if applicable law in your jurisdiction imposes age requirements on the use of communication software or data processing services, you are responsible for ensuring compliance.\n\nOrganisations registering on behalf of minors (e.g., schools) are responsible for ensuring that their use of the Service complies with all applicable child data protection laws, including but not limited to the General Data Protection Regulation (GDPR) and Hungarian national data protection legislation."
                )
            }

            item {
                TosSection("6. Account Registration and Security")
                TosBody(
                    "To use the Service, you must register an account. You agree to:\n\n• Provide accurate, current, and complete registration information.\n• Maintain the confidentiality of your password and account credentials.\n• Notify Vezérfonal immediately of any unauthorised access to your account.\n• Accept responsibility for all activity that occurs under your account.\n\nVezérfonal will not be liable for any loss or damage arising from unauthorised use of your account credentials."
                )
            }

            item {
                TosSection("7. Organisational Accounts and Administrators")
                TosBody(
                    "Organisations using the Service designate one or more administrators who are responsible for managing access, creating and managing groups, and sending messages within the organisation. Administrators are responsible for:\n\n• Ensuring that their organisation's use of the Service complies with these Terms and applicable law.\n• Managing user access and promptly revoking access for users who should no longer have it.\n• The content sent or distributed through the Service by users under their organisation.\n\nVezérfonal is not responsible for the internal governance of any organisation using the Service."
                )
            }

            item {
                TosSection("8. User Content")
                TosSubsection("8.1 Ownership")
                TosBody("You retain full ownership of all messages, content, and files you submit, send, or transmit through the Service (\"User Content\"). Vezérfonal claims no ownership over any User Content.")
                TosSubsection("8.2 License to Operate the Service")
                TosBody("By using the Service, you grant Vezérfonal a limited, non-exclusive, royalty-free, worldwide licence to store, process, and transmit your User Content solely to the extent necessary to provide and operate the Service. This licence does not permit Vezérfonal to use your User Content for any other purpose, including advertising, training of machine learning models, or disclosure to third parties, except as required by law or as described in these Terms.")
                TosSubsection("8.3 Responsibility for User Content")
                TosBody("You are solely responsible for your User Content. Vezérfonal does not moderate, screen, or endorse User Content, and acts solely as a passive conduit for the transmission and storage of messages between users.")
            }

            item {
                TosSection("9. Acceptable Use")
                TosBody(
                    "You agree not to use the Service to transmit, store, or distribute any content that:\n\n• Violates any applicable Hungarian or European Union law or regulation, including but not limited to laws on hate speech, harassment, defamation, intellectual property, and data protection.\n• Is fraudulent, deceptive, threatening, abusive, or harassing toward any person.\n• Constitutes or facilitates the distribution of unsolicited commercial messages (spam).\n• Contains malicious code, viruses, or any software designed to damage, interfere with, or gain unauthorised access to any system.\n• Infringes the intellectual property rights of any third party.\n• Violates the privacy of any individual, including the unauthorised processing or disclosure of personal data.\n\nVezérfonal reserves the right to investigate complaints and to take action, including the suspension or termination of accounts, where there is credible evidence of a violation of these Terms or applicable law."
                )
            }

            item {
                TosSection("10. Intermediary Liability")
                TosBody(
                    "Vezérfonal operates as a hosting service provider within the meaning of the EU Digital Services Act (Regulation (EU) 2022/2065). Vezérfonal does not proactively monitor User Content. In accordance with Article 6 of the DSA, Vezérfonal is not liable for User Content provided it had no actual knowledge of illegal content and acts expeditiously upon obtaining such knowledge.\n\nIf you believe that content transmitted through the Service is illegal or violates these Terms, you may notify Vezérfonal at the contact address provided in Section 18. Upon receipt of a valid notification, Vezérfonal will assess the report and take appropriate action, which may include removal of the content and suspension of the responsible account."
                )
            }

            item {
                TosSection("11. Privacy and Data Protection")
                TosSubsection("11.1 Data Collected")
                TosBody("Vezérfonal collects only the personal data necessary for the operation of the Service. This currently includes: email address, display name, profile picture (optional), organisational membership, group assignments, messages sent and received, message statuses, and device tokens used solely for the delivery of push notifications. Vezérfonal does not collect personal data beyond what is operationally necessary and has no intention of doing so in the future. Where new features require additional data, users will be informed before such data is collected.")
                TosSubsection("11.2 Legal Basis")
                TosBody("Personal data is processed on the basis of contractual necessity (Article 6(1)(b) GDPR) — that is, to perform the Service you have agreed to use.")
                TosSubsection("11.3 Data Storage")
                TosBody("All data is stored on servers physically located in Hungary and is subject to EU data protection law.")
                TosSubsection("11.4 Third-Party Services")
                TosBody("Vezérfonal uses Firebase Cloud Messaging (a Google LLC service) solely to deliver push notifications to registered devices. Only device tokens necessary for notification delivery are shared with Firebase. No personal message content, identifiers, or other personal data are transmitted to Firebase. No other third-party data processors are used.")
                TosSubsection("11.5 Your Rights")
                TosBody("As a data subject under the GDPR, you have the right to access, rectify, and erase your personal data. You may request account deletion at any time through the application. Upon deletion request, Vezérfonal will issue a notice confirming the scheduled deletion date and time. Following that date, all data associated with your account will be permanently and irreversibly deleted. Where an organisation is deleted, all data associated with that organisation, including the accounts of its members, will be deleted in the same manner.\n\nYou also have the right to lodge a complaint with the Hungarian National Authority for Data Protection and Freedom of Information (NAIH) if you believe your data protection rights have been violated.")
                TosSubsection("11.6 Data Retention")
                TosBody("Data is retained for as long as your account is active. No data is retained after account deletion beyond what may be required by mandatory Hungarian or EU legal obligations.")
            }

            item {
                TosSection("12. Intellectual Property of Vezérfonal")
                TosBody(
                    "All software, design, trademarks, logos, and other intellectual property constituting the Vezérfonal platform belong to Gubacsi Áron Máté and Balogh Márk. These Terms do not grant you any rights in Vezérfonal's intellectual property beyond the limited right to use the Service in accordance with these Terms."
                )
            }

            item {
                TosSection("13. Service Availability and Modifications")
                TosBody(
                    "Vezérfonal does not guarantee uninterrupted access to the Service. Vezérfonal reserves the right to modify, suspend, or discontinue any part of the Service at any time. During the beta period, no notice obligation applies. For paid service users, terms of availability will be specified in the individual service contract."
                )
            }

            item {
                TosSection("14. Termination")
                TosSubsection("14.1 By You")
                TosBody("You may terminate your account at any time by requesting account deletion through the application.")
                TosSubsection("14.2 By Vezérfonal")
                TosBody("Vezérfonal may suspend or terminate your account at any time if:\n\n• You violate any provision of these Terms.\n• You violate any applicable Hungarian or EU law through your use of the Service.\n• Your continued use poses a legal, security, or operational risk to the Service or to other users.\n\nPrior to termination (unless an immediate threat exists), Vezérfonal will endeavour to provide notice and an opportunity to remedy the violation where reasonably practicable.")
                TosSubsection("14.3 Effect of Termination")
                TosBody("Upon termination, your right to access the Service ceases immediately. Data deletion will follow the process described in Section 11.5.")
            }

            item {
                TosSection("15. Limitation of Liability")
                TosBody(
                    "To the fullest extent permitted by Hungarian and EU law:\n\n• The Service is provided as-is and as-available, without warranties of any kind, express or implied, including but not limited to warranties of fitness for a particular purpose, merchantability, or non-infringement.\n• Vezérfonal is not liable for any loss or damage arising from: the content of messages sent by users; decisions made on the basis of information transmitted through the Service; unauthorised access to accounts resulting from user negligence; service interruptions; or data loss during the beta period.\n• Vezérfonal's total liability to you for any claim arising out of or in connection with these Terms or the Service shall not exceed the total fees paid by you to Vezérfonal in the twelve months preceding the event giving rise to the claim. During the beta period, this amount is zero.\n\nNothing in these Terms limits or excludes liability for fraud, gross negligence, wilful misconduct, or any other liability that cannot be excluded under mandatory applicable law."
                )
            }

            item {
                TosSection("16. Governing Law and Dispute Resolution")
                TosBody(
                    "These Terms are governed by the laws of Hungary. Any dispute arising out of or in connection with these Terms shall be subject to the exclusive jurisdiction of the competent courts of Hungary."
                )
            }

            item {
                TosSection("17. Changes to These Terms")
                TosBody(
                    "Vezérfonal may update these Terms at any time. Where changes are material, users will be notified via the application or email at least 14 days before the changes take effect. Your continued use of the Service after the effective date of any changes constitutes your acceptance of the updated Terms. If you do not accept the updated Terms, you must discontinue use of the Service."
                )
            }

            item {
                val uriHandler = LocalUriHandler.current
                TosSection("18. Contact")
                TosBody("For questions, complaints, data requests, or legal notices, contact:")
                Text(
                    text = "legal@vezerfonal.org",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("mailto:legal@vezerfonal.org")
                    },
                )
                TosBody("Gubacsi Áron Máté & Balogh Márk")
                Spacer(Modifier.height(Spacing.xl))
            }
        }
    }
}

@Composable
private fun TosSection(title: String) {
    Spacer(Modifier.height(Spacing.md))
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun TosSubsection(title: String) {
    Spacer(Modifier.height(Spacing.sm))
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun TosBody(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
